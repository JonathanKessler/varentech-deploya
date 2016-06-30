package com.varentech.deploya.Form;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

// TODO: Tav: lots of info logging needs to be debug logging.
// TODO: Tav: classnames start with a capital letter!
// TODO: Tav: several e.printStackTrace()s that need to be logg.error()s.
// TODO: Tav: why is this package capitalized? It's cool if there is a valid
// reason. Just wondering.
// TODO: Tav: Potentially can be broken down into smaller methods to improve
// readability, but not necessary.
public class saveTempDirectory {

    private Logger logg = LoggerFactory.getLogger(saveTempDirectory.class);

    // TODO: Tav: header comment. The name of this method isn't very descriptive
    void directory(String file_name){
        Resource res = new Resource();

        try {
            //saves file in temporary directory
            InputStream inputStream = new FileInputStream(res.entry.getPathToDestination() + File.separator + file_name);
            File temp = File.createTempFile("temp","");
            temp.delete();
            temp.mkdir();
            logg.info("Temporary directory has been made: {}" , temp);

            OutputStream outputStream = null;
            outputStream = new FileOutputStream(temp + File.separator + file_name);
            IOUtils.copy(inputStream, outputStream);
            logg.info("{} has been copied to the temporary directory.", file_name);
            outputStream.close();

            File[] files = temp.listFiles();
            File current = files[0];

            //unpack the file into the temporary directory
            ProcessFile pro = new ProcessFile();
            File tempDir = pro.unpack(current);
            logg.info("{} has been unpacked into the temporary directory.", file_name);
            File[] tempDirList = tempDir.listFiles();

            //moves unpacked files to destination if user checked directory
            if(res.entry.getUnpackArguments()!=null){
                for(File file: tempDirList){

                    if(file != current){


                        if(file.isDirectory() ==false) {
                            inputStream = new FileInputStream(file);
                            outputStream = new FileOutputStream(res.entry.getPathToDestination() + File.separator + file.getName());
                            IOUtils.copy(inputStream, outputStream);
                            outputStream.close();
                        }else {
                            File srcDir = new File(res.entry.getPathToDestination() + File.separator + file.getName());
                            FileUtils.copyDirectory(file, srcDir);
                        }
                    }
                }
                logg.info("{} has been unpacked into the destination.", file_name);
            }


            //execute jar/tar file and save output. Only execute if there was no error while unpacking
            if(res.entriesDetail.getError()=="" || (res.entriesDetail.getError()!="" && res.entry.getUnpackArguments()==null)) {
                pro.executeArguments();
                logg.info("{} has been executed.", file_name);
            }

            //find hash of all files
            pro.hashFiles(tempDirList, current);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
