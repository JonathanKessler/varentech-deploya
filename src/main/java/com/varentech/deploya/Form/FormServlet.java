package com.varentech.deploya.Form;

import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
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
            EntriesDetailsDoaImpl impl = new EntriesDetailsDoaImpl();
            SendFile send = new SendFile();
            ProcessFile process = new ProcessFile();

            String file_name = null;
            String path_to_destination = null;
            String execute_args = null;
            String unpack_args = null;
            String archive = null;
            FileItem fileItem = null;

            //get all parameters from the form
            try {
                List<FileItem> multiparts = null;
                multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                String inputName = null;
                for(FileItem item : multiparts){

                    //gets file item from form
                    if(!item.isFormField()){
                        file_name = new File(item.getName()).getName();
                        fileItem = item;
                    }
                    //gets all other parameters from form
                    if(item.isFormField()){
                        inputName = (String)item.getFieldName();
                        if(inputName.equalsIgnoreCase("path_to_destination")){
                            path_to_destination = (String)item.getString();
                        } else if(inputName.equalsIgnoreCase("execute_args")){
                            execute_args = (String)item.getString();
                        } else if(inputName.equalsIgnoreCase("unpack_args")){
                            unpack_args = (String)item.getString();
                        } else if(inputName.equalsIgnoreCase("archive")){
                            archive = (String)item.getString();
                        }
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }

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
            try {
                fileItem.write( new File(path_to_destination + File.separator + file_name));
            } catch (Exception e) {
                e.printStackTrace();
            }

            logg.info("Successfully sent file to destination");

            //save file to archive
            /*
            if(archive!=null) {
                GetConfigProps property= new GetConfigProps();
                try {
                    fileItem.write(new File(property.getSetting("default_directory") + File.separator + res.entry.getFileName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                logg.info("Successfully sent file to destination");
            }
*/

            //unpack file if necessary
            if(unpack_args!=null) {
                process.unpackArchiveArguments(file_name);
            }

            //set hash value
            //might be finding these in FindAllFileNames
            //hash.getHash();

            //execute jar/tar file and save output. Only execute if there was no error while unpacking
            if(res.entriesDetail.getError()==null) {
                process.executeArguments();
            }

            //add entriesDetail to database
            impl.insertIntoEntriesDetail(res.entriesDetail);

            //find all file names and hash codes for subfiles of a tar
            //ProcessFile.findAllFileNames();

            //send output and error to the screen (error will appear in red)
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<body>");
            if(res.entriesDetail.getOutput()!=null) {
                out.println("<font color=”000000”>" + res.entriesDetail.getOutput() + "</font>");
                out.println("<br>");
            }
            if(res.entriesDetail.getError()!=null) {
                out.println("<font color=”ff0000”>" + res.entriesDetail.getError() + "</font>");
            }
            out.println("</body>");
            out.println("</html>");


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
