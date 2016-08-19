package com.varentech.deploya.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class ConfigurationFileManipulation {
    private static final Logger logg = LoggerFactory.getLogger(ConfigurationFileManipulation.class);
    private String path;
    public ConfigurationFileManipulation(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void exportConfigFile() {
        InputStream inputStream = getClass().getResourceAsStream("/reference.conf");

        logg.debug("Reading internal config file.");
        try {
            //Getting the default .properties file as a File object.
            URL url = Resources.getResource("reference.conf");

            String text = Resources.toString(url, Charsets.UTF_8);
            System.out.println(text);

            try {
                File destination_file = new File(path);
                OutputStream outputStream;
                outputStream = new FileOutputStream(destination_file);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                logg.debug("Successfully copied reference.conf to " + getPath());
            } catch (FileNotFoundException e) {
                logg.error("Exception while finding file to path: , " + e);
            } catch (IOException e) {
                logg.error("Exception while sending file to path: , " + e);
            }

        } catch (IOException e) {
            logg.error("Exception while exporting config: ", e);
        }
    }
}
