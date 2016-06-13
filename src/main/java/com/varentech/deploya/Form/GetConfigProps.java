package com.varentech.deploya.Form;

import java.io.IOException;
import java.io.InputStream;
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
    private static final Properties prop = new Properties();
    /**
     *
     * @return String that has the the value of result????
     * @throws IOException
     */

    //public void getPropValues() throws IOException {
        static {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                prop.load(loader.getResourceAsStream("config.properties"));
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }


        public static String getSetting(String key) {
            return prop.getProperty(key);
        }



        /*
        try {

            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            System.out.println(prop.getProperty("port_number") );
            Enumeration enuKeys = prop.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = prop.getProperty(key);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
*/
       // return result;


}
