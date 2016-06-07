package com.varentech.deploya.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfiguration {
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
