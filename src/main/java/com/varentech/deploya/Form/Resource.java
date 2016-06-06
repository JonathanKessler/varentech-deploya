package com.varentech.deploya.Form;

import com.varentech.deploya.Driver;
import com.varentech.deploya.entities.EntriesDetail;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("home")

public class Resource {

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("file_name") String file_name, @FormParam("path_to_destination") String path_to_destination, @FormParam("execute_args") String execute_args, @FormParam("unpack_args") String unpack_args) {

        System.out.println(file_name);
        System.out.println(path_to_destination);
        System.out.println(execute_args);
        System.out.println(unpack_args);

        EntriesDetail entry = new EntriesDetail();
        entry.setFileName(file_name);
        entry.setPathToDestination(path_to_destination);
        entry.setExecuteArguments(execute_args);
        entry.setUnpackArguments(unpack_args);

        Driver drive = new Driver();
        drive.exampleArgs(entry);

        /*InsertToDatabase db = new InsertToDatabase();
        db.addToDatabase(file_name, path_to_destination, execute_args, unpack_args);
*/
        return "<h2> Hello, " + path_to_destination + "</h2>";
    }
}