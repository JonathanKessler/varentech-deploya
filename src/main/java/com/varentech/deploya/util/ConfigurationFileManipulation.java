package com.varentech.deploya.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;


public class ConfigurationFileManipulation {
    static Logger logg = LoggerFactory.getLogger(ConfigurationFileManipulation.class);
    String path;
    public ConfigurationFileManipulation(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void exportConfigFile() {
        InputStream inputStream = getClass().getResourceAsStream("/config.properties");
        BufferedReader input = new BufferedReader(new InputStreamReader((inputStream)));
        String line = null;

        logg.debug("Reading internal config file.");
        try {

            //Getting the default .properties file as a File object.
            URL url = Resources.getResource("config.properties");
            File internalConfig = new File(url.getPath());

            File exportedPropertiesFile = new File(path);
            //exportedPropertiesFile.createNewFile();


            String text = Resources.toString(url, Charsets.UTF_8);
            System.out.println(text);

            //Copy the contents of the internalConfig to the desired properties file.
            //Files.copy(inputStream, exportedPropertiesFile.toPath());

            try {
                File destination_file = new File(path);
                OutputStream outputStream;
                outputStream = new FileOutputStream(destination_file);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                logg.info("Successfully copied config.properties to " + getPath());
            } catch (FileNotFoundException e) {
                logg.error("Exception while finding file to path: , " + e);
            } catch (IOException e) {
                logg.error("Exception while sending file to path: , " + e);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importConfigFile() throws IOException {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec("jar -uf " + getPath());
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (Exception e) {
            logg.error("Exception thrown while trying jar -uf " + getPath());
        }
        logg.info(output.toString());
    }
}

