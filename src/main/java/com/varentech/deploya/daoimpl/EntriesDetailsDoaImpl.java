package com.varentech.deploya.daoimpl;

import com.varentech.deploya.dao.DatabaseInterface;
import com.varentech.deploya.entities.EntriesDetail;
import com.varentech.deploya.util.ConnectionConfiguration;

import java.sql.*;
import java.util.List;

public class EntriesDetailsDoaImpl implements DatabaseInterface {

  public void createTable() {
    //Create two table called "Entries" & "Entries_Details" with
    //the necessary columns so we can properly insert them in the
    //database.
  }


  public void insert(EntriesDetail entriesDetail) {
    try {
      Connection connection = ConnectionConfiguration.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO Entries " +
                      "(time_stamp, username, file_name, path_to_local_file, path_to_destination, unpack_args, execute_args) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)"
      );
      preparedStatement.setString(1, entriesDetail.getTime().toString());
      preparedStatement.setString(2, entriesDetail.getUserName());
      preparedStatement.setString(3, entriesDetail.getFileName());
      preparedStatement.setString(4, entriesDetail.getPathToLocalFile());
      preparedStatement.setString(5, entriesDetail.getPathToDestination());
      preparedStatement.setString(6, entriesDetail.getUnpackArguments());
      preparedStatement.setString(7, entriesDetail.getExecuteArguments());
      preparedStatement.executeUpdate();
    } catch (SQLException se) {
      se.printStackTrace();
    }
    //Select the Entries id and storing that into a local variable using the time stamp.
    try {
      Connection connection = ConnectionConfiguration.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
              "SELECT id FROM Entries WHERE time_stamp = " + "'" + entriesDetail.getTime() + "'"
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
