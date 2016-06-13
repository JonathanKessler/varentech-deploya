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
 * @see java.lang.Process
 * @author Varen Technologies
 */

public class ProcessFile {

  private Logger logger = LoggerFactory.getLogger(ProcessFile.class);

  /**
   * Execute a some sort of program with the given execute command and arguments.
   * Also prints the standard output and standard error if such are produced from this given
   * program.
   * @throws Exception if an invalid execute command is given.
   */
  public void executeArguments() {

    try {

      Resource res = new Resource();
      String output = "";
      //Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\kesslerk\\Documents\\HelloWorldJar\\out\\artifacts\\HelloWorldJar_jar\\HelloWorldJar.jar");

      logger.debug("Execute command is: " + res.entry.getExecuteArguments());

      Process p = Runtime.getRuntime().exec(res.entry.getExecuteArguments());
      BufferedReader in = new BufferedReader(
              new InputStreamReader(p.getInputStream()));
      String line = null;

      logger.debug("The standard output stream is: ");

      while ((line = in.readLine()) != null) {
        //System.out.println(line);
        output= output + line;
      }

      String stdErr = "";
      BufferedReader stdErrReader = new BufferedReader(
              new InputStreamReader(p.getErrorStream()));

      logger.debug("The standard error stream is: ");

      while((line = stdErrReader.readLine()) != null){
        stdErr = stdErr + line;
      }

      //save output
      res.entriesDetail.setOutput(output);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Unpack some given archive file with the given unpacking arguments.
   * Also prints the standard error if such error occurred.
   * @throws Exception if an invalid unpacking argument is given.
   */
  public void unpackArchiveArguments(){
    try{
      //Create new Resource object to get the unpack commands.
      Resource res = new Resource();

      logger.debug("The unpacking arguments are: " + res.entry.getUnpackArguments());

      //Create a new Process to handle the process command given from res.
      //Process process = Runtime.getRuntime().exec(res.entry.getUnpackArguments());

      Process process = Runtime.getRuntime().exec(res.entry.getUnpackArguments());

      String line = null;
      String stdErr = "";
      BufferedReader stdErrReader = new BufferedReader(
              new InputStreamReader(process.getErrorStream()));
      while((line = stdErrReader.readLine()) != null){
        stdErr = stdErr + line;
      }


    }catch(IOException e){
      e.printStackTrace();
    }
  }

  /**
   *
   * @param file
   * @return String[] of file names that are in the given file.
   */

  public ArrayList<String> findAllFileNames(File file) {
    Resource resource = new Resource();

    ArrayList<String> fileNames = new ArrayList<String>();
    int counter = 0;

    if (file.isDirectory()){
      String[] directoryContents = file.list();
      for(int i = 0; i < directoryContents.length; i++){
        fileNames.add(directoryContents[i]);
      }
    }
    String fileExtenstion = file.toString();

    if (fileExtenstion.contains("tar")){
      //List all files in the the tar.gz or tar file.
      try{
        Process process = Runtime.getRuntime().exec("tar -tvf " + resource.entry.getFileName());
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while((line = in.readLine()) != null){
          fileNames.add(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }else if(fileExtenstion.contains("zip")){
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
    }else{
      logger.error("Incompatible extension type!");
    }

    return fileNames;
  }
}



