package com.varentech.deploya;

import com.varentech.deploya.Form.ExecuteJar;
import com.varentech.deploya.Form.ServerConnection;
import com.varentech.deploya.Form.ServerConnection;
import com.varentech.deploya.doaimpl.EntriesDetailsDoaImpl;
import com.varentech.deploya.entities.EntriesDetail;

import java.util.Date;

public class Driver {


  //This string should be final as it should not change. At initial run, the
  //program should be able to create this directory if it is not found.
  final static String pathToLocal = "/opt/deploya";

  public static void main(String[] args){

    //connect to server
    ServerConnection server = new ServerConnection();
    server.connect();

    //EntriesDetailsDoaImpl eddi = new EntriesDetailsDoaImpl();
    //EntriesDetail entriesDetail = new EntriesDetail();

    //A new instance of Date must be called everytime at run.
    //Date date = new Date();

    //TODO: Make sure that a directory is located at /opt/deploya/

    //TODO: Create that directory if it does not exist.

    //TODO: Find a way to get the username so we can add it to the database.
    //Get username Code
    //entriesDetail.setUserName();

    //Storing the date in the entriesDetail object.
    //entriesDetail.setTime(date);

    //TODO: Find a way to get the file name
    //Get filename
    //entriesDetail.setFileName();

    //Store the path of the local saved file.
    //entriesDetail.setPathToLocalFile(pathToLocal);

    //TODO: Find a way to store where the given file will be stored.
    //Get desired file location
    //entriesDetail.setPathToDestination();

    //TODO: Find a way to get the unpacking arguments. If none given, default of null.
    //Get unpack arguments.
    //entriesDetail.setUnpackArguments();

    //TODO: Find a way to get the executable arguments.

    //TODO: Save the file to a location in the computer given.

    //TODO: Find a way to save and print the output of the program.

  }

  public void exampleArgs(EntriesDetail entry){
    Date date = new Date();

    entry.setUserName("Bob");
    entry.setTime(date);
    //entry.setHashValue("123");
    entry.setOutput("no error");
    entry.setPathToLocalFile("over there");

    EntriesDetailsDoaImpl impl = new EntriesDetailsDoaImpl();
    impl.insert(entry);

    ExecuteJar ex = new ExecuteJar();
    ex.exJar();

  }

}
