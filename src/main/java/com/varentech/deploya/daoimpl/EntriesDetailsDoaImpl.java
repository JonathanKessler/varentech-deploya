package com.varentech.deploya.daoimpl;

import com.varentech.deploya.form.Resource;
import com.varentech.deploya.dao.DatabaseInterface;
import com.varentech.deploya.entities.EntriesDetail;
import com.varentech.deploya.util.ConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


/**
 * Connects to Deploya.db in order to add to the data tables
 */
public class EntriesDetailsDaoImpl implements DatabaseInterface {

    /**
     * This method inserts into the Entries table.
     * This table holds data from user input.
     */
     Logger logg = LoggerFactory.getLogger(EntriesDetailsDaoImpl.class);
  public void insertIntoEntries() {

    Resource res = new Resource();

    try {
      Connection connection = ConnectionConfiguration.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO Entries " +
                      "(time_stamp, username, file_name, path_to_local_file, path_to_destination, unpack_args, execute_args,archive) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?,?)"
      );
      preparedStatement.setString(1, res.entry.getTime().toString());
      preparedStatement.setString(2, res.entry.getUserName());
      preparedStatement.setString(3, res.entry.getFileName());
      preparedStatement.setString(4, res.entry.getPathToLocalFile());
      preparedStatement.setString(5, res.entry.getPathToDestination());
      preparedStatement.setString(6, res.entry.getUnpackArguments());
      preparedStatement.setString(7, res.entry.getExecuteArguments());
      preparedStatement.setString(8, res.entry.getArchive());
      preparedStatement.executeUpdate();
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

    } catch (SQLException e) {
    logg.error("Exception while inserting entries details into database: ", e);
    }
  }

  
}
