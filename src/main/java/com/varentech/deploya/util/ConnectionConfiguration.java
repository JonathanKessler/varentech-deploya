package com.varentech.deploya.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class connects to the database.
 */
public class ConnectionConfiguration {

    private static String pathToDataBase;

    //setter
    public static void setPathToDataBase(String path){
        pathToDataBase = path;
    }
    //getter
    public static String getPathToDataBase(){
        return pathToDataBase;
    }

    /**
     * This method establishes a connection to a database.
     */
    private final static Logger logg = LoggerFactory.getLogger(ConnectionConfiguration.class);
    public static Connection getConnection() {
        Connection connection = null;

        try {
            //Get the SQLite JDBC class.
            Class.forName("org.sqlite.JDBC");
            //connect to the database given by the path
            connection = DriverManager.getConnection("jdbc:sqlite:" + getPathToDataBase());
        } catch (Exception e) {
            logg.error("Exception while connecting to database: ", e);
        }
        return connection;
    }
}
