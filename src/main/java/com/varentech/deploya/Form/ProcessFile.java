package com.varentech.deploya.Form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class manages the given Linux shell commands and prints the stdOutput and stdErr of both.
 *
 * @author Varen Technologies
 * @see java.lang.Process
 */

public class ProcessFile {

    private Logger logger = LoggerFactory.getLogger(ProcessFile.class);

    /**
     * Execute a some sort of program with the given execute command and arguments.
     * Also prints the standard output and standard error if such are produced from this given
     * program.
     *
     * @throws Exception if an invalid execute command is given.
     */

    public void executeArguments() {

        try {

            Resource res = new Resource();
            String output = "";

            Process p = Runtime.getRuntime().exec(res.entry.getExecuteArguments());
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;

            while ((line = in.readLine()) != null) {
                output = output + line;
            }

            String stdErr = "";
            BufferedReader stdErrReader = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            while ((line = stdErrReader.readLine()) != null) {
                stdErr = stdErr + line;
            }

            //save output and error
            res.entriesDetail.setOutput(output);
            res.entriesDetail.setError(stdErr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unpack some given archive file with the given unpacking arguments.
     * Also prints the standard error if such error occurred.
     *
     * @throws Exception if an invalid unpacking argument is given.
     */
    public void unpackArchiveArguments(String file_name) {

        Resource res = new Resource();
        int dot = file_name.indexOf('.');
        String fileExtension = file_name.substring(dot);
        Process p=null;

        try {
            if (fileExtension.equals(".jar")) {
                p = Runtime.getRuntime().exec("jar xf " + res.entry.getPathToDestination() + File.pathSeparator + file_name);
            }else if (fileExtension.equals(".tar")) {
                p = Runtime.getRuntime().exec("tar -xf " + res.entry.getPathToDestination() + File.pathSeparator + file_name);
            } else if (fileExtension.equals(".tar.gz") || fileExtension.equals(".tar.tgz")){
                p = Runtime.getRuntime().exec("tar -xcf " + res.entry.getPathToDestination() + File.pathSeparator + file_name);
            } else if (fileExtension.equals(".zip")){
                p = Runtime.getRuntime().exec("unzip " + res.entry.getPathToDestination() + File.pathSeparator + file_name);
            }else{
                logger.error("Incorrect file extension");
            }

            String stdErr = "";
            String line;
            BufferedReader stdErrReader = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            while ((line = stdErrReader.readLine()) != null) {
                stdErr = stdErr + line;
            }

            res.entriesDetail.setError(stdErr);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     * @return String[] of file names that are in the given file.
     */

    public ArrayList<String> findAllFileNames(File file) {
        Resource resource = new Resource();

        ArrayList<String> fileNames = new ArrayList<String>();
        int counter = 0;

        if (file.isDirectory()) {
            String[] directoryContents = file.list();
            for (int i = 0; i < directoryContents.length; i++) {
                fileNames.add(directoryContents[i]);
            }
        }
        String fileExtenstion = file.toString();

        if (fileExtenstion.contains("tar")) {
            //List all files in the the tar.gz or tar file.
            try {
                Process process = Runtime.getRuntime().exec("tar -tvf " + resource.entry.getFileName());
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    fileNames.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (fileExtenstion.contains("zip")) {
            //List all the files in a zip archive.
            try {
                Process process = Runtime.getRuntime().exec("unzip -l " + resource.entry.getFileName());
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    fileNames.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("Incompatible extension type!");
        }

        return fileNames;
    }
}

