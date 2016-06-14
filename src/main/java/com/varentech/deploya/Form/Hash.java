package com.varentech.deploya.Form;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.*;

/**
 * This class creates a new Hash value that changes everytime so that each file that is different gets a new hash value.
 * @author VarenTech
 * @see org.apache.commons.codec.digest.DigestUtils
 * @see java.io.InputStream
 * @see java.io.File
 * @see java.io.FileInputStream
 * @see java.io.IOException
 * @see java.io.FileNotFoundException
 * @see java.security.MessageDigest
 * @see java.security.NoSuchAlgorithmException
 * @see java.util.logging.Logger
 *
 *
 */
//http://javarevisited.blogspot.com/2013/06/how-to-generate-md5-checksum-for-files.html
public class Hash {
    private static final Logger logger = Logger.getLogger(Hash.class.getName());

    public static String checkSum(File file) {
        //String file = "C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar"; //location of the file

        String hash_value = null;
        try {
            hash_value = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return hash_value;
    }


    public String createSha1(File file)  {
        //File file = new File(file_name);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            InputStream fis = new FileInputStream(file);
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return digest.digest().toString();
    }


    public void getHash(){


        try {
            String hashCommand = "CertUtil -hashfile C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar MD5";
            Process p = Runtime.getRuntime().exec(hashCommand);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            String output = "";

            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                output= output + line;
            }

            System.out.println(output);
            Resource res = new Resource();
            res.entriesDetail.setHashValue(output);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}

