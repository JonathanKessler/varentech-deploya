package com.varentech.deploya.dao;

import com.varentech.deploya.entities.EntriesDetail;

import java.util.List;

/*
This interface consists of a few methods that could be used for a database
implementation.

@author parksw
@see  List
@since 1.0
 */

public interface DatabaseInterface {
  /*
  This method is used to create a new table in the
  database if needed.
   */
  void createTable();
  /*
  <p>This method is used to insert into the database
  with the parameter EntriesDetail type object.

  @param EntriesDetail  the object that needs to be inserted into the database.
   */
  void insert(EntriesDetail entriesDetail);
  /*
  <p>This method is used to list down
  all the record from the Entries table or the
  Entries_Details table.

  @return a list of the entries found in the database.
   */
  List<EntriesDetail> listEntries();


}
