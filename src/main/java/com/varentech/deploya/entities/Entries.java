package com.varentech.deploya.entities;

/**
 * Entries class that holds all information for the Entries table.
 */
public class Entries {

    private String userName;
    private String fileName;
    private String time;
    private String pathToLocalFile;
    private String pathToDestination;
    private String unpackArguments;
    private String executeArguments;
    private String archive;

    //Default Constructor
    public Entries(){}

    //Constructor
    public Entries(
            String userName, String fileName, String time, String pathToLocalFile,
            String pathToDestination, String unpackArguments, String executeArguments, String archive){
        this.userName = userName;
        this.fileName = fileName;
        this.time = time;
        this.pathToLocalFile = pathToLocalFile;
        this.pathToDestination = pathToDestination;
        this.unpackArguments = unpackArguments;
        this.executeArguments = executeArguments;
        this.archive=archive;
    }

    //Setters
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void setTime(String time){
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
    public void setArchive(String archive){this.archive = archive;}

    //Getters
    public String getUserName(){
        return userName;
    }
    public String getFileName(){
        return fileName;
    }
    public String getTime(){
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
    public String getArchive() { return archive; }

}
