package com.varentech.deploya.Form;

import com.varentech.deploya.Driver;
import com.varentech.deploya.directories.LocalDirectories;
import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import com.varentech.deploya.entities.Entries;
import com.varentech.deploya.entities.EntriesDetail;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("home")

public class Resource {

    public static Entries entry = new Entries();
    public static EntriesDetail entriesDetail = new EntriesDetail();

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("file_name") String file_name,
                             @FormParam("path_to_destination") String path_to_destination,
                             @FormParam("execute_args") String execute_args,
                             @FormParam("unpack_args") String unpack_args) {

        Resource res = new Resource();
        Hash h = new Hash();
        LocalDirectories local = new LocalDirectories();
        EntriesDetailsDoaImpl impl = new EntriesDetailsDoaImpl();
        Driver drive = new Driver();

        //fomat data for timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//format the date to have no spaces
        String formatted_time = formatter.format(date);


        //add to entry table
        res.entry.setPathToDestination(path_to_destination);
        res.entry.setExecuteArguments(execute_args);
        res.entry.setUnpackArguments(unpack_args);
        res.entry.setTime(formatted_time);
        res.entry.setFileName(res.renaming(file_name));
        res.entry.setUserName("Bob");
        res.entry.setPathToLocalFile("here");
        //res.entry.setUserName();
        //res.entry.setPathToLocalFile();

        //add entry to database
        impl.insertIntoEntries();

        //add file name to entriesDetail
        res.entriesDetail.setFileName(res.entry.getFileName());


        //set hash value
        File file = new File(res.entry.getFileName());
        String hash = ""+file.hashCode();
        res.entriesDetail.setHashValue(hash);

        //unpack if necessary
        Unpack un = new Unpack();
        //unpack._____();


        //execute jar/tar file and save output
        ExecuteJar ex = new ExecuteJar();
        ex.exJar();

        //add entriesDetail to database
        impl.insertIntoEntriesDetail(res.entriesDetail);

        //find all file names and hash codes for subfiles of a tar
        ex.findAllFileNames();



        //res.entry.setHashValue(h.checkSum());
        //drive.exampleArgs();

        return "<h2>" + res.entriesDetail.getOutput() + "</h2>";
    }

    public String renaming (String file_name){ //method to add timestamp to file name
        int dot = file_name.lastIndexOf('.');
        String time_stamped = file_name.substring(0,dot) + "_" + entry.getTime() + file_name.substring(dot);
        return time_stamped;
    }


}