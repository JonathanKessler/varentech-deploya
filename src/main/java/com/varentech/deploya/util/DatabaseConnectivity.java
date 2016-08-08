package com.varentech.deploya.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseConnectivity {

    static Logger logg = LoggerFactory.getLogger(DatabaseConnectivity.class);


    /**
     * No args method calls upon the local config file for database path.
     */
    public static void findDatabase() {

        //Load our own config values from the default location
        //application.conf
        Config config = ConfigFactory.load();
        logg.debug("Database name: " + config.getString("varentech.project.path_to_database"));
        String pathOfDataBase = config.getString("varentech.project.path_to_database");
        ConnectionConfiguration.setPathToDataBase(pathOfDataBase);
    }

    /**
     * @param path a String that provides the path to a database on the filesystem.
     */
    public static void findDataBase(String path){
        //Load the given config values given from the arguments
        Config config = ConfigFactory.load(path);
        logg.debug("Loading values from the config file located at " + path);
        String pathOfDataBase = config.getString("varentech.project.path_to_database");
        try{
            logg.info("Loading database path given at " + pathOfDataBase);
            //setting path of database to ConnectionConfiguration
            ConnectionConfiguration.setPathToDataBase(pathOfDataBase);
            //need to check to see if the tables exist
            databaseCheck();
        }
        catch(Exception e){
            logg.error("File not found at " + pathOfDataBase);
            logg.debug("Using default values to create a database.");
            findDatabase();
        }
    }

    public static void databaseCheck() throws SQLException {
        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()){
            logg.debug("Entries table exists");
            //check to see if all of columns exists in Entries table.
            String tempColName;
            resultSet = metaData.getColumns(null, null, "Entries", "time_stamp");
            tempColName = "time_stamp";
            if(resultSet.next()){logg.debug("time_stamp column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "username");
            tempColName = "username";
            if(resultSet.next()){logg.debug("username column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "file_name");
            tempColName = "file_name";
            if(resultSet.next()){logg.debug("file_name column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "path_to_local_file");
            tempColName = "path_to_local_file";
            if(resultSet.next()){logg.debug("path_to_local_file column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "path_to_destination");
            tempColName = "path_to_destination";
            if(resultSet.next()){logg.debug("path_to_destination column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "unpack_args");
            tempColName = "unpack_args";
            if(resultSet.next()){logg.debug("unpack_args column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "execute_args");
            tempColName = "execute_args";
            if(resultSet.next()){logg.debug("execute_args column exists");}
            resultSet = metaData.getColumns(null, null, "Entries", "archive");
            tempColName = "archive";
            if(resultSet.next()){logg.debug("archive column exists");}
            else{
                logg.error("Necessary column, " + tempColName + " does not exist in Entries table.");
                logg.error("Program will exit with Error code 1.");
                System.exit(1);
            }
        }
        else{
            logg.debug("Entries table does not exist, creating Entries table...");
            EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
            impl.createEntriesTable();
        }
        //check for Entries_Details table
        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if(resultSet.next()){
            logg.debug("Entries_Details table exists");
            //Need to check that each column exists in Entries_Details table
            String tempColName;
            resultSet = metaData.getColumns(null, null, "Entries_Details", "entries_id");
            tempColName = "entries_id";
            if(resultSet.next()){logg.debug("entries_id column exists");}
            resultSet = metaData.getColumns(null, null, "Entries_Details", "file_name");
            tempColName = "file_name";
            if(resultSet.next()){logg.debug("file_name column exists");}
            resultSet = metaData.getColumns(null, null, "Entries_Details", "hash_value");
            tempColName = "hash_value";
            if(resultSet.next()){logg.debug("hash_value column exists");}
            resultSet = metaData.getColumns(null, null, "Entries_Details", "output");
            tempColName = "output";
            if(resultSet.next()){logg.debug("output column column exists");}
            resultSet = metaData.getColumns(null, null, "Entries_Details", "error");
            tempColName = "error";
            if (resultSet.next()){logg.debug("error column exists");}
            else{
                logg.error("Necessary column, " + tempColName + " does not exist in Entries_Details table.");
                logg.error("Program will exit with Error code 1.");
                System.exit(1);
            }
        }
        else{
            logg.debug("Entries_Details table does not exits, creating Entries_Details table...");
            EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
            impl.createEntriesDetailsTable();
        }
    }

}
