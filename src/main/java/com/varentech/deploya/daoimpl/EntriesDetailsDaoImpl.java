package com.varentech.deploya.daoimpl;

import com.varentech.deploya.form.Resource;
import com.varentech.deploya.dao.DatabaseInterface;
import com.varentech.deploya.entities.EntriesDetail;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Connects to Deploya.db in order to add to the data tables.
 */
public class EntriesDetailsDaoImpl implements DatabaseInterface {

    /**
     * This method inserts into the Entries table.
     * This table holds data from user input.
     */
    private static final Logger logg = LoggerFactory.getLogger(EntriesDetailsDaoImpl.class);

    public void createEntriesTable() {
        Connection connection = ConnectionConfiguration.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE Entries " +
                            "(id INTEGER PRIMARY KEY NOT NULL , " +
                            "time_stamp           TEXT    NOT NULL," +
                            "username             TEXT    NOT NULL," +
                            "file_name            TEXT    NOT NULL," +
                            "path_to_local_file   TEXT    ," +
                            "path_to_destination  TEXT    NOT NULL," +
                            "unpack_args          TEXT    ," +
                            "execute_args         TEXT    NOT NULL," +
                            "archive              TEXT            )"
            );
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            logg.error("Unable to create the table Entries.", e);
        }
        logg.debug("Successfully created Entries table.");

    }

    public void createEntriesDetailsTable() {
        Connection connection = ConnectionConfiguration.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE Entries_Details " +
                            "(id INTEGER PRIMARY KEY NOT NULL ," +
                            "entries_id   INT    NOT NULL," +
                            "file_name    TEXT   NOT NULL," +
                            "hash_value   TEXT   NOT NULL," +
                            "output       TEXT           ," +
                            "error        TEXT           )"
            );
            preparedStatement.executeUpdate();
            logg.debug("Successfully created the Entries_Details table.");
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            logg.error("Unable to create Entries_Details table.", e);
        }
    }

    public void insertColumnsIntoEntries() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            //Drops the given table, and calls createEntriesTable with appropriate columns
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DROP TABLE Entries;"
            );
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            createEntriesTable();
        } catch (SQLException s) {
            logg.error(s.getSQLState(), s);
        }
    }

    public void insertColumnsIntoEntriesDetails() {
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            //Drops the given table, and calls createEntriesDetailsTable with appropriate columns
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DROP TABLE Entries_Details;"
            );
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            createEntriesDetailsTable();
        } catch (SQLException s) {
            logg.error(s.getSQLState());
        }
    }

    public void insertIntoEntries() {

        Resource res = new Resource();

        try {
            Connection connection = ConnectionConfiguration.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Entries " +
                            "(time_stamp, username, file_name, path_to_local_file, path_to_destination, unpack_args, execute_args,archive) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, res.entry.getTime().toString());
            preparedStatement.setString(2, res.entry.getUserName());
            preparedStatement.setString(3, res.entry.getFileName());
            preparedStatement.setString(4, res.entry.getPathToLocalFile());
            preparedStatement.setString(5, res.entry.getPathToDestination());
            preparedStatement.setString(6, res.entry.getUnpackArguments());
            preparedStatement.setString(7, res.entry.getExecuteArguments());
            preparedStatement.setString(8, res.entry.getArchive());
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();

            logg.debug("Successfully inserted entries into database.");
        } catch (SQLException e) {
            logg.error("Exception while inserting entries into database: ", e);
        }
    }

    /**
     * This method inserts into the Entries_Details table using a parameter of an EntriesDetail object.
     * This table holds the data found about the files.
     */
    public void insertIntoEntriesDetail(EntriesDetail entriesDetail) {

        Resource res = new Resource();

        //Select the Entries id and storing that into a local variable using the time stamp.
        try {
            Connection connection = ConnectionConfiguration.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT id FROM Entries WHERE time_stamp = " + "'" + res.entry.getTime() + "'"
            );
            int id = resultSet.getInt("id");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Entries_Details " +
                            "(entries_id, file_name, hash_value, output, error)" +
                            "VALUES (?,?,?,?,?)"
            );

            preparedStatement.setString(1, String.valueOf(id));
            preparedStatement.setString(2, entriesDetail.getFileName());
            preparedStatement.setString(3, String.valueOf(entriesDetail.getHashValue()));
            preparedStatement.setString(4, String.valueOf(entriesDetail.getOutput()));
            preparedStatement.setString(5, String.valueOf(entriesDetail.getError()));
            preparedStatement.executeUpdate();

            preparedStatement.close();
            resultSet.close();
            statement.close();
            connection.close();

            logg.debug("Successfully inserted " + entriesDetail.getFileName() + "entries details into database");
        } catch (SQLException e) {
            logg.error("Exception while inserting entries details into database: ", e);
        }
    }
}
