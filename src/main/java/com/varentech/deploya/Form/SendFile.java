package com.varentech.deploya.Form;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.*;

/*
*This class saves a file in the destination directory and archive if necessary
*
 */
public class SendFile {

    private Logger logger = LoggerFactory.getLogger(SendFile.class);


    /*
    *Takes the InputStream of the file from the form and saves it into a file in the destination directory
    * @param Part from the HTTP request containing the file information
    */
    void sendToDestination(FileItem fileItem, String file_name){

        Resource res = new Resource();

        try {
            fileItem.write( new File(res.entry.getPathToDestination() + File.separator + file_name));
            logger.info("Successfully sent file to destination");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
    void sendToArchive(String file_name){

        Resource res = new Resource();
        GetConfigProps properties = new GetConfigProps();

        try {
            InputStream inputStream = new FileInputStream(res.entry.getPathToDestination() + File.separator + file_name);
            File destination_file = new File(properties.getSetting("default_directory")+ File.separator + res.entry.getFileName());
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(destination_file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();

            logger.info("Successfully archived to default directory");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
