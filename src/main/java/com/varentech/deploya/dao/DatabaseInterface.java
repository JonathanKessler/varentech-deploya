package com.varentech.deploya.dao;

import com.varentech.deploya.entities.EntriesDetail;

/**
 *Contains methods to add to the database.
 */
public interface DatabaseInterface {
    /**
     *Insert into the Entries table of Deploya.db.
     */
    void insertIntoEntries();

    /**
     *Insert into the EntriesDetails table of Deploya.db.
     */
    void insertIntoEntriesDetail(EntriesDetail entriesDetail);
}
