package com.varentech.deploya.directories;

import java.io.File;
/*
This class consists of methods that can be used for creating directories
and for storing our file in our local directory.
 */
public class LocalDirectories {

/*
These paths should not be changed, unless we want to save the jar/tar file somewhere else.
 */

  private final String windowsPath = "C:\\Users\\VarenTech\\Deploya\\";
  private final String linuxPath = "/opt/VarenTech/Deploya/";


  /*
  @return A string in lower case what the operating system is.
   */

  public String getOS(){
    String OS = System.getProperty("os.name").toLowerCase();
    return OS;
  }

  /*
  @return File locations where the local temporary file is located.
   */

  public File createAndGetLocalDirectoriesNames(){
    String OS = getOS();
    File directories = null;
    if(isWindows(OS)){
      directories = new File(windowsPath);
      if(!directories.exists()){
        System.out.println("Directories did not exist, so they were created.");
      }else{
        System.out.println("Directories already exist!");
      }
    }
    else if(isLinux(OS)){
      directories = new File(linuxPath);
      if(!directories.exists()){
        System.out.println("Directories did not exist, so they were created.");
      }
      else{
        System.out.println("Directories already exist!");
      }
    }
    return directories;
  }

  /*
  @param File a file object where the local directory is located at.
  @param String the name of the file name that is being uploaded to the server.
   */
  public void addToDirectory(File directories, String fileName){
    File file = new File(directories, fileName);
  }

  /*
  @return boolean if the current operating system this is being run on is Linux.
 */
  public boolean isLinux(String OS){
    return (OS.indexOf("linux") >= 0);
  }

  /*
  @return boolean if the current operating system this is being run on is Windows.
   */
  public boolean isWindows(String OS){
    return (OS.indexOf("win") >= 0);
  }
}
