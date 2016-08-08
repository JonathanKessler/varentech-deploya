package com.varentech.deploya.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (columnCount == 0){
                //TODO: Need to create the columns of the table.
            }
            //check to see if all of columns exists in Entries table.
            String tempColName = "id";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "time_stamp";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "username";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "file_name";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "path_to_local_file";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "path_to_destination";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "unpack_args";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "execute_args";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "archive";
            columnCheck(hasColumn(resultSet, tempColName));

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
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (columnCount == 0){
                //TODO: Need to create the columns of the table.
            }
            //Need to check that each column is properly named in Entries_Details table
            String tempColName = "id";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "entries_id";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "file_name";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "hash_value";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "output";
            columnCheck(hasColumn(resultSet, tempColName));
            tempColName = "error";
            columnCheck(hasColumn(resultSet, tempColName));
        }
        else{
            logg.debug("Entries_Details table does not exits, creating Entries_Details table...");
            EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
            impl.createEntriesDetailsTable();
        }
    }

    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
    public static void columnCheck(boolean check){
        if(check == true){

            return;
        }
        else{
            logg.error("Column does not exist in this table!");
            logg.error("System will exit with code 1.");
            System.exit(1);
        }
    }
}
