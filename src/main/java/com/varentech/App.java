package com.varentech;

import com.varentech.daoimpl.EntriesDoaImpl;
import com.varentech.daoimpl.MusicDaoImpl;
import com.varentech.entities.Entries;
import com.varentech.entities.Music;

/**
 * Created by ParksW on 5/25/2016.
 */
public class App {
    public static void main(String[] args){
        MusicDaoImpl mdi = new MusicDaoImpl();
        mdi.createMusicTable();
        Music music = new Music();
        mdi.insert(music);

        EntriesDoaImpl edi = new EntriesDoaImpl();
        edi.createEntryTable();



    }
}
