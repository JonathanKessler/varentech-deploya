package com.varentech.deploya.Form;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class GetConfigProps {
    String result = " ";
    InputStream inputStream;

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

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String port_number = prop.getProperty("port_number");
            String default_directory = prop.getProperty("default_directory");
            String log_level=prop.getProperty("log_level");
            String acceptable_extensions=prop.getProperty("acceptable_extensions");

            result = "Configurations: " + port_number + " ," + default_directory + " ," + log_level + " ," +acceptable_extensions ;
            System.out.println(result + "\nProgram Ran on " + time );

            //if you have a lot of key-value pairs this way is much simpler,
            //still need to find out how to deal with acceptable extensions being a list
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
