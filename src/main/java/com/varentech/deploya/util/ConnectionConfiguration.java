package com.varentech.deploya.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class manages a connection to the database.
 * @author VarenTech
 * @see java.sql.Connection
 * @see java.sql.DriverManager
 */
public class ConnectionConfiguration {

  /**
   * This method establishes a connection to a database, that can be configurable.
   * @return Connection to the database, or an error if there was a problem connecting to it.
     */

  //TODO: Make the database configurable through our Config file?
  public static Connection getConnection(){
    Connection connection = null;

    try{
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:Thunder.db");
    }catch (Exception e){
      e.printStackTrace();
    }
    return connection;
  }
}
