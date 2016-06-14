package com.varentech.deploya.Form;

import org.apache.commons.compress.utils.IOUtils;
import javax.servlet.http.Part;
import java.io.*;

/*
*This class saves a file in the destination directory and archive if necessary
*
 */
public class SendFile {

    /*
    *Takes the InputStream of the file from the form and saves it into a file in the destination directory
    * @param Part from the HTTP request containing the file information
    */
    void sendToDestination(Part part){

        Resource res = new Resource();

        try {
            InputStream inputStream = part.getInputStream();
            File destination_file = new File(res.entry.getPathToDestination() + File.separator + part.getSubmittedFileName());
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(destination_file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void sendToArchive(){

    }
}
