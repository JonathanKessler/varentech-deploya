package com.varentech.dao;

import com.varentech.entities.Entries;

import java.util.List;

/**
 * Created by ParksW on 5/26/2016.
 */
public interface EntriesDao {
    void createEntryTable();
    void insert(Entries entries);
    Entries selectById(int id);
    List<Entries> selectAll();

    void delete(int id);
    void update(Entries entries, int id);
}
