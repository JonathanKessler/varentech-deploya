package com.varentech.deploya.form;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;

import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a servlet for the form page to gather information from the user.
 */
public class FormServlet extends HttpServlet {
    Resource res = new Resource();
    private static final Logger logg = LoggerFactory.getLogger(FormServlet.class);

    /**
     * This method runs when the submit button is clicked on the form.jsp.
     * Calls all methods necessary to process the file.
     * Inserts information into our database.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        response.setContentType("text/html");
        logg.debug("Successfully connected to form servlet.");
        response.setStatus(HttpServletResponse.SC_OK);

        EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
        SendFile send = new SendFile();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        String default_directory = homeDirectory(config.getString("default_directory"));
        String context_path = config.getString("context_path");
        String port = config.getString("port_number");

        String file_name = null;
        String path_to_destination = null;
        String execute_args = null;
        String unpack_args = null;
        String archive = null;
        FileItem fileItem;
        InputStream fileInputStream = null;

        //get all parameters from the form
        try {
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            String inputName;
            for (FileItem item : multiparts) {

                //gets file item from form
                if (!item.isFormField()) {
                    file_name = new File(item.getName()).getName();
                    fileItem = item;
                    try {
                        fileInputStream = fileItem.getInputStream();
                    } catch (IOException e) {
                        logg.error("Exception while getting file item from form: ", e);
                    }
                }
                //gets all other parameters from form
                if (item.isFormField()) {
                    inputName = item.getFieldName();
                    if (inputName.equalsIgnoreCase("path_to_destination")) {
                        path_to_destination = homeDirectory(item.getString());
                    } else if (inputName.equalsIgnoreCase("execute_args")) {
                        execute_args = homeDirectory(item.getString());
                    } else if (inputName.equalsIgnoreCase("unpack_args")) {
                        unpack_args = item.getString();
                    } else if (inputName.equalsIgnoreCase("archive")) {
                        archive = item.getString();
                    }
                }
            }
        } catch (FileUploadException e) {
            logg.error("Exception while uploading file: ", e);
        }

        //format data for timestamp
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //format the date to have no spaces
        String formatted_time = formatter.format(date);

        //add to entry object
        res.entry.setPathToDestination(path_to_destination);
        res.entry.setExecuteArguments(execute_args);
        res.entry.setUnpackArguments(unpack_args);
        res.entry.setTime(formatted_time);
        res.entry.setFileName(renaming(file_name));
        res.entry.setUserName(session.getAttribute("Username").toString());
        res.entry.setArchive(archive);

        if (archive != null) {
            res.entry.setPathToLocalFile(default_directory);
        }

        //add entry to database
        impl.insertIntoEntries();

        //add file name to entriesDetail object
        res.entriesDetail.setFileName(res.entry.getFileName());

        //saves file to destination
        send.sendToDestination(fileInputStream, file_name);

        //if they want to archive, then send file to archive destination


        if (archive != null) {
            boolean arch = createDirectoryArchive(default_directory);
            if (arch) {
                send.sendToArchive(fileInputStream);
            }
        }

        //save all files to a temporary directory in order to directory and get hash, and execute
        SaveTempDirectory dir = new SaveTempDirectory();
        dir.directory(file_name);

        //add entriesDetail to database
        impl.insertIntoEntriesDetail(res.entriesDetail);

        //redirect to the output
        logg.debug("Now redirecting to file output page.");
        try {
            response.sendRedirect("http://" + request.getServerName() + ":" + port + context_path + "/output.jsp");
        } catch (IOException e) {
            logg.error("Error while redirecting to file output: ", e);
        }
    }

    /**
     * Returns a new String with the file_name plus the timestamp at the end of it. This version will be saved in the archive.
     */
    public static String renaming(String file_name) { //method to add timestamp to file name
        Resource res = new Resource();
        String time_stamped;
        int dot = file_name.indexOf('.');

        if (dot == -1) {
            time_stamped = file_name + "_" + res.entry.getTime();
        } else {
            time_stamped = file_name.substring(0, dot) + "_" + res.entry.getTime() + file_name.substring(dot);
        }
        return time_stamped;
    }

    public static String homeDirectory(String directory) {
        String home = System.getProperty("user.home");
        if (home != null) {
            directory = directory.replace("~", home);
        }
        return directory;
    }

    public void createDirectoryDB(String path) {
        File paths = new File(path);
        File parent = new File(paths.getParent());
        if (!parent.exists() || !parent.isDirectory()) {
            new File(parent.toString()).mkdir();
            logg.info("Directory has been made: " + parent);
        }
    }

    public boolean createDirectoryArchive(String path) {
        File paths = new File(path);
        File parent = new File(paths.getParent());
        if (parent.exists()) {
            if (!paths.exists() || !paths.isDirectory()) {
                new File(paths.toString()).mkdir();
                logg.info("Directory has been made: " + paths);
                return true;
            }
        } else {
            logg.error("Error " + parent + " does not exist. Could not archive.");
            res.entriesDetail.setError("Was not able to archive." + parent + " does not exist. " + res.entriesDetail.getError());
            return false;
        }
        return false;
    }
}
