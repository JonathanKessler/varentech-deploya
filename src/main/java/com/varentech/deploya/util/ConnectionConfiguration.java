package com.varentech.deploya.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.form.SendFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class manages a connection to the database.
 */
public class ConnectionConfiguration {

    private static Logger logg = LoggerFactory.getLogger(SendFile.class);
    /**
     * This method establishes a connection to a database, that can be configurable.
     * @return Connection to the database, or an error if there was a problem connecting to it.
     */

    private static Config config = ConfigFactory.load();
    private static String databasePath = config.getString("varentech.project.path_to_database");

    public static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath );

        }catch (Exception e){
            e.printStackTrace();
        }
        logg.debug("Successfully created and/or connected to the database.");
        return connection;
    }
}
