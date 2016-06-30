package com.varentech.deploya.util;

import java.sql.Connection;
import java.sql.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
