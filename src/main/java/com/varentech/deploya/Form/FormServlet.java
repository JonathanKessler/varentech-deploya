package com.varentech.deploya.Form;

import com.varentech.deploya.directories.LocalDirectories;
import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is a servlet for the Form html where after a user logs into the web app,
 * this is where we will easily store their data into our database.
 *
 * @author VarenTech
 * @see com.varentech.deploya.Driver
 * @see com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl
 * @see javax.servlet.ServletException
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.http.HttpServletRequest
 * @see javax.servlet.http.HttpServletResponse
 * @see javax.servlet.http.HttpSession
 * @see java.io.IOException
 * @see java.text.SimpleDateFormat
 * @see java.util.Date
 *
 */

public class FormServlet {

    /**
     * Creates a servlet to the form page so that we can get information from the client when they type
     * their commands in the HTML form. This also inserts the information into our database, while they enter it.
     *
     */
    public static class formServlet extends HttpServlet {
        Resource res = new Resource();
        FormServlet form = new FormServlet();
        private final Logger logg = LoggerFactory.getLogger(FormServlet.class);

        //this method runs when the submit button is clicked on the main form
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("text/html");
            logg.info("Successfully connected to form servlet.");
            response.setStatus(HttpServletResponse.SC_OK);

            HttpSession session = request.getSession();
            Hash hash = new Hash();
            LocalDirectories local = new LocalDirectories();
            EntriesDetailsDoaImpl impl = new EntriesDetailsDoaImpl();
            SendFile send = new SendFile();
            ProcessFile process = new ProcessFile();

            //get all parameters from the form
            String file_name = request.getPart("file_name").getSubmittedFileName();
            String path_to_destination = request.getParameter("path_to_destination");
            String execute_args = request.getParameter("execute_args");
            String unpack_args = request.getParameter("unpack_args");
            String archive = request.getParameter("archive");
            logg.info("Filename is {} Execute this way: {} Unpack this way {} Archive? {}", file_name,execute_args,unpack_args,archive);

            //fomat data for timestamp
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//format the date to have no spaces
            String formatted_time = formatter.format(date);

            //add to entry object
            res.entry.setPathToDestination(path_to_destination);
            res.entry.setExecuteArguments(execute_args);
            res.entry.setUnpackArguments(unpack_args);
            res.entry.setTime(formatted_time);
            res.entry.setFileName(form.renaming(file_name));
            res.entry.setUserName(session.getAttribute("Username").toString());
            res.entry.setArchive(archive);
            if(archive!=null) {
                GetConfigProps property= new GetConfigProps();
                res.entry.setPathToLocalFile(property.getSetting("default_directory"));
            }
            //add entry to database
            impl.insertIntoEntries();
            logg.info("Successfully added entries to database.");

            //add file name to entriesDetail object
            res.entriesDetail.setFileName(res.entry.getFileName());

            //saves file to destination
            Part part = request.getPart("file_name");
            send.sendToDestination(part);
            logg.info("Successfully sent file to destination.");

            //unpack file if necessary
            //process.unpackArchiveArguments();

            //set hash value
            //might be finding these in FindAllFileNames
            //hash.getHash();

            //execute jar/tar file and save output
            //process.executeArguments();

            //add entriesDetail to database
            impl.insertIntoEntriesDetail(res.entriesDetail);

            //find all file names and hash codes for subfiles of a tar
            //ex.findAllFileNames();

            //send output to the screen
            response.getWriter().println(res.entriesDetail.getOutput());

        }
    }

    /**
     *
     * @param file_name
     * @return a new String with the file_name plus the timestamp at the end of it. This version will be saved in the archive.
     */

    //TODO: Save this file_name + timestamp file into the archive directory.
    public String renaming(String file_name) { //method to add timestamp to file name
        Resource res = new Resource();
        int dot = file_name.lastIndexOf('.');
        String time_stamped = file_name.substring(0, dot) + "_" + res.entry.getTime() + file_name.substring(dot);
        return time_stamped;
    }
}
