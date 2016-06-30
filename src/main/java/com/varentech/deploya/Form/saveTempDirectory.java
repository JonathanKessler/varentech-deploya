package com.varentech.deploya.form;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

/**
 *This class has methods to save all files into a temporary directory and the move those files out to the temporary directory.
 */
public class SaveTempDirectory {

    private Logger logg = LoggerFactory.getLogger(SaveTempDirectory.class);
    
    /**
     *This method creates and saves all files to a temporary directory
     * Calls methods to unpack files, move files to destination directory, execute files, and find the hash of all files.
     */
    void directory(String file_name){
        Resource res = new Resource();

        try {
            //saves file in temporary directory
            InputStream inputStream = new FileInputStream(res.entry.getPathToDestination() + File.separator + file_name);
            File temp = File.createTempFile("temp","");
            temp.delete();
            temp.mkdir();
            logg.debug("Temporary directory has been made: {}" , temp);

            OutputStream outputStream = null;
            outputStream = new FileOutputStream(temp + File.separator + file_name);
            IOUtils.copy(inputStream, outputStream);
            logg.debug("{} has been copied to the temporary directory.", file_name);
            outputStream.close();

            File[] files = temp.listFiles();
            File current = files[0];

            //unpack the file into the temporary directory
            ProcessFile pro = new ProcessFile();
            File tempDir = pro.unpack(current);
            logg.debug("{} has been unpacked into the temporary directory.", file_name);
            File[] tempDirList = tempDir.listFiles();

            //moves unpacked files to destination if user checked directory
            moveFiles(tempDirList, current);
            logg.info("{} has been unpacked into the destination.", file_name);

            //execute jar/tar file and save output. Only execute if there was no error while unpacking
            if(res.entriesDetail.getError()=="" || (res.entriesDetail.getError()!="" && res.entry.getUnpackArguments()==null)) {
                pro.executeArguments();
                logg.debug("{} has been executed.", file_name);
            }

            //find hash of all files
            pro.hashFiles(tempDirList, current);

        } catch (FileNotFoundException e) {
            logg.error("Exception while finding file: ", e);
        } catch (IOException e) {
            logg.error("Exception while saving or moving file to temporary directory: ", e);
        }
    }
    
    /**
     *This method moves unpacked files into the destination directory if the user checked the box for unpacking.
     */
    public void moveFiles(File[] tempDirList, File current) {

        Resource res = new Resource();

        if (res.entry.getUnpackArguments() != null) {
            for (File file : tempDirList) {

                if (file != current) {

                    try {
                        if (file.isDirectory() == false) {
                            InputStream inputStream = null;

                            inputStream = new FileInputStream(file);

                            OutputStream outputStream = new FileOutputStream(res.entry.getPathToDestination() + File.separator + file.getName());
                            IOUtils.copy(inputStream, outputStream);
                            outputStream.close();
                        } else {
                            File srcDir = new File(res.entry.getPathToDestination() + File.separator + file.getName());
                            FileUtils.copyDirectory(file, srcDir);
                        }
                    } catch (FileNotFoundException e) {
                        logg.error("Exception while finding file: ", e);
                    } catch (IOException e) {
                        logg.error("Exception while saving or moving file to temporary directory: ", e);
                    }
                }
            }
        }
    }

}
