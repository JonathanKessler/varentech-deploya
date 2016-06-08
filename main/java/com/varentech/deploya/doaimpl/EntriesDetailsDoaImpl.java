package com.varentech.deploya.doaimpl;

import com.varentech.deploya.Form.Resource;
import com.varentech.deploya.doa.DatabaseInterface;
import com.varentech.deploya.entities.Entries;
import com.varentech.deploya.entities.EntriesDetail;
import com.varentech.deploya.util.ConnectionConfiguration;

import java.sql.*;
import java.util.List;

public class EntriesDetailsDoaImpl implements DatabaseInterface {

 /* public void createTable() {
    //Create two table called "Entries" & "Entries_Details" with
    //the necessary columns so we can properly insert them in the
    //database.
  }
*/

  public void insertIntoEntries() {

    Resource res = new Resource();

    try {
      Connection connection = ConnectionConfiguration.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO Entries " +
                      "(time_stamp, username, file_name, path_to_local_file, path_to_destination, unpack_args, execute_args) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)"
      );
      preparedStatement.setString(1, res.entry.getTime().toString());
      preparedStatement.setString(2, res.entry.getUserName());
      preparedStatement.setString(3, res.entry.getFileName());
      preparedStatement.setString(4, res.entry.getPathToLocalFile());
      preparedStatement.setString(5, res.entry.getPathToDestination());
      preparedStatement.setString(6, res.entry.getUnpackArguments());
      preparedStatement.setString(7, res.entry.getExecuteArguments());
      preparedStatement.executeUpdate();
    } catch (SQLException se) {
      se.printStackTrace();
    }

  }

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
                      "(entries_id, file_name, hash_value, output)" +
                      "VALUES (?,?,?,?)"
      );

      preparedStatement.setString(1, String.valueOf(id));
      preparedStatement.setString(2, entriesDetail.getFileName());
      preparedStatement.setString(3, String.valueOf(entriesDetail.getHashValue()));
      preparedStatement.setString(4, String.valueOf(entriesDetail.getOutput()));
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //TODO: Perhaps add a prepared statement that lists all of the data in the entire database.
  public List<EntriesDetail> listEntries() {
    return null;
  }
}
