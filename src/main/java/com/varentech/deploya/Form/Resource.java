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

/**
 * This class creates a resource object that creates objects the two different tables.
 * @author VarenTech
 * @see com.varentech.deploya.Driver;
 * @see com.varentech.deploya.directories.LocalDirectories;
 * @see com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
 * @see com.varentech.deploya.entities.Entries;
 * @see com.varentech.deploya.entities.EntriesDetail;
 * @see javax.ws.rs.*;
 * @see javax.ws.rs.core.MediaType;
 * @see java.io.File;
 * @see java.text.SimpleDateFormat;
 * @see java.util.Date;
 */

public class Resource {

    //create objects for the database
    public static Entries entry = new Entries();
    public static EntriesDetail entriesDetail = new EntriesDetail();

   /* @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("file_name") File file,
                             @FormParam("path_to_destination") String path_to_destination,
                             @FormParam("execute_args") String execute_args,
                             @FormParam("unpack_args") String unpack_args) {

  */
}