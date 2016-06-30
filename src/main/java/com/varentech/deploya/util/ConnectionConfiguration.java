package com.varentech.deploya.util;

import java.sql.Connection;
import java.sql.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class connects to the database.
 */
public class ConnectionConfiguration {

   /**
   * This method establishes a connection to a database.
   */
  private static Logger logg = LoggerFactory.getLogger(ConnectionConfiguration.class);
  public static Connection getConnection(){
    Connection connection = null;

    try{
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:Deploya.db");
    }catch (Exception e){
     logg.error("Exception while connecting to database: ", e);
    }
    return connection;
  }
}
