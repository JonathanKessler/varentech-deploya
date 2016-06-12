package com.varentech.deploya.entities;

/**
 * Class that holds the values of all the Entries_Details values.
 * @author VarenTech
 */
public class EntriesDetail {

  private String fileName;
  private String hashValue;
  private String output;

  //Default Constructor
  public EntriesDetail(){}

  //Constructor
  public EntriesDetail(
          String fileName, String hashValue, String output){
    this.fileName = fileName;
    this.hashValue = hashValue;
    this.output = output;
  }

  //Setters
  public void setFileName(String fileName){
    this.fileName = fileName;
  }
  public void setHashValue(String hashValue){
    this.hashValue = hashValue;
  }
  public void setOutput(String output){
    this.output = output;
  }

  //Getters
  public String getFileName(){return fileName;}
  public String getHashValue(){
    return hashValue;
  }
  public String getOutput(){
    return output;
  }


}