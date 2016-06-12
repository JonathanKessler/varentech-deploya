package com.varentech.deploya.doaimpl;

import com.varentech.deploya.Form.Resource;
import com.varentech.deploya.doa.DatabaseInterface;
import com.varentech.deploya.entities.EntriesDetail;
import com.varentech.deploya.util.ConnectionConfiguration;
import java.sql.*;
import java.util.List;

/**
 *
 * This class first connects to our db, then with the given methods, enters specific parameters into different columns in
 * either the Entries table or Entries_Details table.
 * Note: The Entries Table is information that the client provides via web app.
 * The Entries_Details Table is information that this program does on the backend.
 *
 * @author VarenTech
 *
 * @see com.varentech.deploya.util.ConnectionConfiguration
 * @see List
 * @see java.sql.PreparedStatement
 * @see java.sql.Connection
 */

public class EntriesDetailsDoaImpl implements DatabaseInterface {


  /**
   * This method inserts into the Entries table. It calls upon the Resource class
   * to insert easily into the table.
   */

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
    } catch (SQLException se) {
      se.printStackTrace();
    }

  }

  /**
   * This method inserts into the Entries_Details table using a parameter of an EntriesDetail object.
   * @param entriesDetail
   *
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


  /**
   * Auto-generated from the interface.
   */
  public void createTable() {

  }

  /**
   * Auto-generated from interface.
   *
     */
  public void insert(EntriesDetail entriesDetail) {

  }

  //TODO: Perhaps add a prepared statement that lists all of the data in the entire database.
  public List<EntriesDetail> listEntries() {
    return null;
  }
}
