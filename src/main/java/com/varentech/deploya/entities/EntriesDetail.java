package com.varentech.deploya.entities;

import java.util.Date;

public class EntriesDetail {
  private String userName;
  private String fileName;
  private Date time = new Date();
  private String pathToLocalFile;
  private String pathToDestination;
  private String unpackArguments;
  private String executeArguments;
  private String hashValue;
  private String output;

  public EntriesDetail(){}

  public EntriesDetail(
          String userName, String fileName, Date time, String pathToLocalFile,
          String pathToDestination, String unpackArguments, String executeArguments,
          String hashValue, String output){
    this.userName = userName;
    this.fileName = fileName;
    this.time = time;
    this.pathToLocalFile = pathToLocalFile;
    this.pathToDestination = pathToDestination;
    this.unpackArguments = unpackArguments;
    this.executeArguments = executeArguments;
    this.hashValue = hashValue;
    this.output = output;
  }

  //Setters
  public void setUserName(String userName){
    this.userName = userName;
  }
  public void setFileName(String fileName){
    this.fileName = fileName;
  }
  public void setTime(Date time){
    this.time = time;
  }
  public void setPathToLocalFile(String pathToLocalFile){
    this.pathToLocalFile = pathToLocalFile;
  }
  public void setPathToDestination(String pathToDestination){
    this.pathToDestination = pathToDestination;
  }
  public void setUnpackArguments(String unpackArguments){
    this.unpackArguments = unpackArguments;
  }
  public void setExecuteArguments(String executeArguments){
    this.executeArguments = executeArguments;
  }
  public void setHashValue(String hashValue){
    this.hashValue = hashValue;
  }
  public void setOutput(String output){
    this.output = output;
  }
  //Getters
  public String getUserName(){
    return userName;
  }
  public String getFileName(){
    return fileName;
  }
  public Date getTime(){
    return time;
  }
  public String getPathToLocalFile(){
    return pathToLocalFile;
  }
  public String getPathToDestination(){
    return pathToDestination;
  }
  public String getUnpackArguments(){
    return unpackArguments;
  }
  public String getExecuteArguments(){
    return executeArguments;
  }
  public String getHashValue(){
    return hashValue;
  }
  public String getOutput(){
    return output;
  }


}
