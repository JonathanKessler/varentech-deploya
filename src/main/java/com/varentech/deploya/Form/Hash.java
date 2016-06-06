package com.varentech.deploya.Form;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.util.logging.*;

//http://javarevisited.blogspot.com/2013/06/how-to-generate-md5-checksum-for-files.html
public class Hash {
    private static final Logger logger = Logger.getLogger(Hash.class.getName());

    public static void FileInfo() {

        //System.out.println("MD5 checksum of file in Java using Apache commons is: " + checkSum(file));
    }

    public static String checkSum(String file) {
        //String file = "C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar"; //location of the file

        String hash_value = null;
        try {
            hash_value = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return hash_value;
    }
}

