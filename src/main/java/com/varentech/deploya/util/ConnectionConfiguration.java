package com.varentech.deploya.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class connects to the database.
 */
public class ConnectionConfiguration {
    private static final Logger logg = LoggerFactory.getLogger(ConnectionConfiguration.class);
    private static String pathToDataBase;

    //setter
    public static void setPathToDataBase(final String path) {
        pathToDataBase = path;
    }
    //getter
    private static String getPathToDataBase() {
        return pathToDataBase;
    }

    /**
     * This method establishes a connection to a database.
     */
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
