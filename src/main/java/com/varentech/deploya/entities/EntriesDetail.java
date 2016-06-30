package com.varentech.deploya.entities;

/**
 * Class that holds the values of all the Entries_Details values.
 */
public class EntriesDetail {

  private String fileName;
  private String hashValue;
  private String output;
    private String error;

  //Default Constructor
  public EntriesDetail(){}

  //Constructor
  public EntriesDetail(
          String fileName, String hashValue, String output){
    this.fileName = fileName;
    this.hashValue = hashValue;
    this.output = output;
      this.error=error;
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
    public void setError(String error){ this.error = error;}

  //Getters
  public String getFileName(){return fileName;}
  public String getHashValue(){
    return hashValue;
  }
  public String getOutput(){
    return output;
  }
    public String getError(){return error;}

}
