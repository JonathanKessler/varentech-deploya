package com.varentech.deploya.form;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * This class contains methods to send the file to the destination directory and archive directory (if applicable).
 */
public class SendFile {

    private Logger logg = LoggerFactory.getLogger(SendFile.class);

    /**
     * This method saves the file into the destination directory.
     */
    public void sendToDestination(InputStream inputStream, String file_name) {

        Resource res = new Resource();

        try {
            File destination_file = new File(res.entry.getPathToDestination() + File.separator + file_name);
            OutputStream outputStream;
            outputStream = new FileOutputStream(destination_file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();

            logg.debug("Successfully sent file to destination");

        } catch (IOException e) {
            logg.error("Exception while sending file to destination: ", e);
        }
    }

    /**
     * This method saves the file to the default archive directory is the user checked the box for archive.
     */
    public void sendToArchive(InputStream inputStream) {

        Resource res = new Resource();

        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);

        try {
            File destination_file = new File(config.getString("varentech.project.default_directory") + File.separator + res.entry.getFileName());
            OutputStream outputStream;
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
