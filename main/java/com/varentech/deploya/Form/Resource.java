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

        //fomat data for timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//format the date to have no spaces
        String formatted_time = formatter.format(date);

        Resource res = new Resource();
        Hash h = new Hash();
        LocalDirectories local = new LocalDirectories();
        Driver drive = new Driver();

        //add to entry table
        res.entry.setPathToDestination(path_to_destination);
        res.entry.setExecuteArguments(execute_args);
        res.entry.setUnpackArguments(unpack_args);
        res.entry.setTime(formatted_time);
        res.entry.setFileName(res.renaming(file_name));
        //res.entry.setUserName();

        //add to entriesDetail table
        res.entriesDetail.setFileName(res.entry.getFileName());

        //save file locally
        //File file = local.createAndGetLocalDirectoriesNames();
        //local.addToDirectory(file, res.entry.getFileName());

        //are we unpacking or not
       // Unpack tj = new Unpack();
        //tj.unpack(res.entry.getFileName());


        //execute jar file
        ExecuteJar ex = new ExecuteJar();
        ex.exJar();
        ex.findAllFileNames();



        //res.entry.setHashValue(h.checkSum());
        drive.exampleArgs();

        return "<h2> Hello, " + path_to_destination + "</h2>";
    }

    public String renaming (String file_name){ //method to add timestamp to file name
        int dot = file_name.lastIndexOf('.');
        String time_stamped = file_name.substring(0,dot) + "_" + entry.getTime() + file_name.substring(dot);
        return time_stamped;
    }
}