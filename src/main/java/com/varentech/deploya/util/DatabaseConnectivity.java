package com.varentech.deploya.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.varentech.deploya.daoimpl.EntriesDetailsDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;


public class DatabaseConnectivity {

    private final static Logger logg = LoggerFactory.getLogger(DatabaseConnectivity.class);

    /**
     *
     */
    public static void findDataBase() {
        //Load the given config values given from the arguments
        Config fileConf = ConfigFactory.parseFile(new File("application.conf"));
        Config config = ConfigFactory.load(fileConf);
        logg.debug("Loading values from the config file");
        String pathOfDataBase = config.getString("varentech.project.path_to_database");
        try {
            logg.debug("Loading database path given at " + pathOfDataBase);
            //setting path of database to ConnectionConfiguration
            ConnectionConfiguration.setPathToDataBase(pathOfDataBase);
            //need to check to see if the tables exist
            databaseCheck();
        }
        catch(Exception e) {
            logg.error("File not found at " + pathOfDataBase);
            logg.debug("Using default values to create a database.");
        }
    }

    public static void databaseCheck() throws SQLException {
        DatabaseMetaData metaData = ConnectionConfiguration.getConnection().getMetaData();
        //check to see if Entries table exists.
        ResultSet resultSet = metaData.getTables(null, null, "Entries", null);
        if (resultSet.next()) {
            logg.debug("Entries table exists");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (columnCount == 0) {
                EntriesDetailsDaoImpl dao = new EntriesDetailsDaoImpl();
                dao.insertColumnsIntoEntries();
            }
            else {
                //check to see if all of columns exists in Entries table.
                String tempColName = "id";
                columnCheck(hasColumn("Entries", tempColName));
                tempColName = "time_stamp";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "username";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "file_name";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "path_to_local_file";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "path_to_destination";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "unpack_args";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "execute_args";
                columnCheck(hasColumn("Entries",tempColName));
                tempColName = "archive";
                columnCheck(hasColumn("Entries",tempColName));
            }
        }
        else {
            logg.debug("Entries table does not exist, creating Entries table...");
            EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
            impl.createEntriesTable();
        }
        //check for Entries_Details table
        resultSet = metaData.getTables(null, null, "Entries_Details", null);
        if(resultSet.next()) {
            logg.debug("Entries_Details table exists");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (columnCount == 0) {
                EntriesDetailsDaoImpl dao = new EntriesDetailsDaoImpl();
                dao.insertColumnsIntoEntriesDetails();
            }
            else {
                //Need to check that each column is properly named in Entries_Details table
                String tempColName = "id";
                columnCheck(hasColumn("Entries_Details", tempColName));
                tempColName = "entries_id";
                columnCheck(hasColumn("Entries_Details",tempColName));
                tempColName = "file_name";
                columnCheck(hasColumn("Entries_Details",tempColName));
                tempColName = "hash_value";
                columnCheck(hasColumn("Entries_Details",tempColName));
                tempColName = "output";
                columnCheck(hasColumn("Entries_Details",tempColName));
                tempColName = "error";
                columnCheck(hasColumn("Entries_Details",tempColName));
            }
        }
        else {
            logg.debug("Entries_Details table does not exits, creating Entries_Details table...");
            EntriesDetailsDaoImpl impl = new EntriesDetailsDaoImpl();
            impl.createEntriesDetailsTable();
        }
        resultSet.close();
    }

    public static boolean hasColumn (String table, String columnName) throws SQLException {

        Connection connection = ConnectionConfiguration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + table);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                rs.close();
                statement.close();
                connection.close();
                return true;
            }
        }
        rs.close();
        statement.close();
        connection.close();
        return false;
    }
    public static void columnCheck (boolean check) {
        if (check == true) {

            return;
        }
        else {
            logg.error("Column does not exist in this table!");
            logg.error("System will exit with code 1.");
            System.exit(1);
        }
    }
}

