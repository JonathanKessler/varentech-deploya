package com.varentech.deploya.Form;

import com.varentech.deploya.Driver;
import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import com.varentech.deploya.entities.EntriesDetail;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("home")

public class Resource {

    public static EntriesDetail entry = new EntriesDetail();
    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("file_name") String file_name,
                             @FormParam("path_to_destination") String path_to_destination,
                             @FormParam("execute_args") String execute_args,
                             @FormParam("unpack_args") String unpack_args) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//format the date to have no spaces
        String formatted_time = formatter.format(date);

        Resource r = new Resource();
        Hash h = new Hash();
        Driver drive = new Driver();

        System.out.println(file_name);
        System.out.println(path_to_destination);
        System.out.println(execute_args);
        System.out.println(unpack_args);

        entry.setPathToDestination(path_to_destination);
        entry.setExecuteArguments(execute_args);
        entry.setUnpackArguments(unpack_args);
        entry.setTime(formatted_time);
        entry.setHashValue(h.checkSum());
        entry.setFileName(r.renaming(file_name));

        drive.exampleArgs();

        return "<h2> Hello, " + path_to_destination + "</h2>";
    }

    public String renaming (String file_name){ //method to add timestamp to file name
        int dot = file_name.lastIndexOf('.');
        String time_stamped = file_name.substring(0,dot) + "_" + entry.getTime() + file_name.substring(dot);
        return time_stamped;
    }
}