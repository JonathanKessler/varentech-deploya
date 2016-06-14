package com.varentech.deploya.doa;

import java.util.List;
import com.varentech.deploya.entities.EntriesDetail;

/*
This interface consists of a few methods that could be used for a database
implementation.
 */

public interface DatabaseInterface {
  /*
  This method is used to create a new table in the
  database if needed.
   */
  //void createTable();
  /*
  <p>This method is used to insert into the database
  with the parameter EntriesDetail type object.
  @param EntriesDetail  the object that needs to be inserted into the database.
   */
  void insertIntoEntries();
  void insertIntoEntriesDetail(EntriesDetail entriesDetail);
  /*
  <p>This method is used to list down
  all the record from the Entries table or the
  Entries_Details table.
  @return a list of the entries found in the database.
   */
  List<EntriesDetail> listEntries();


}
