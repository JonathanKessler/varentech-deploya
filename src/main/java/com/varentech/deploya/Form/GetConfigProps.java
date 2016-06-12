package com.varentech.deploya.Form;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 *
 * This class gets properties from the configuration file.
 * @author VarenTech
 * @see java.io.FileNotFoundException
 * @see java.io.IOException
 * @see java.io.InputStream
 * @see java.util.Enumeration
 * @see java.util.Properties
 *
 */

public class GetConfigProps {
    String result = " ";
    InputStream inputStream;

    /**
     *
     * @return String that has the the value of result????
     * @throws IOException
     */
    public String getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Enumeration enuKeys = prop.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = prop.getProperty(key);
                System.out.println(key + ": " + value);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}