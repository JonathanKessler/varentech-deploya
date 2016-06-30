package com.varentech.deploya.form;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ResourceBundle;

/**
 * This class contains methods to send the file to the destination directory and archive directory (if applicable).
 */
public class SendFile {

    private Logger logg = LoggerFactory.getLogger(SendFile.class);

     /**
      * This method saves the file into the destination directory
      */
    void sendToDestination(FileItem fileItem, String file_name){

        Resource res = new Resource();

        try {
            fileItem.write( new File(res.entry.getPathToDestination() + File.separator + file_name));
            logg.debug("Successfully sent file to destination");
        } catch (Exception e) {
           logg.error("Exception while sending file to destination: ", e);
        }
    }

    /**
     * This method saves the file to the default archive directory is the user checked the box for archive.
     */
    void sendToArchive(String file_name){

        Resource res = new Resource();
        ResourceBundle reso = ResourceBundle.getBundle("config");

        try {
            InputStream inputStream = new FileInputStream(res.entry.getPathToDestination() + File.separator + file_name);
            File destination_file = new File(reso.getString("default_directory") + File.separator + res.entry.getFileName());
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(destination_file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();

            logg.debug("Successfully archived to default directory");

        } catch (FileNotFoundException e) {
            logg.error("Exception while finding file to archive: ", e);
        } catch (IOException e) {
            logg.error("Exception while sending file to archive destination: ", e);
        }
    }
}
