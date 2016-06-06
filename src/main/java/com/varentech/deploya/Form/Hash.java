package com.varentech.deploya.Form;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.util.logging.*;


public class Hash {
    private static final Logger logger = Logger.getLogger(Hash.class.getName());

    public static String checkSum() {
        String file = "C:\\Users\\raynorm\\Documents\\HelloWorldJar" +
                "\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar"; //location of the file
        String hash_value = null;
        try {
            hash_value = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return hash_value;
    }
}

