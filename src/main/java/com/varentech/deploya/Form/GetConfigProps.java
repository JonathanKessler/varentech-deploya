package com.varentech.deploya.Form;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * This class gets properties from the configuration file.
 * @author VarenTech
 * @see java.io.IOException
 * @see java.util.Properties
 *
 */

public class GetConfigProps {

    private static final Properties prop = new Properties();
    /**
     * @loads config file
     * @throws IOException
     */
    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            prop.load(loader.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * @return String property of corresponding key value
     */
    public static String getSetting(String key) {
        return prop.getProperty(key);
    }

}