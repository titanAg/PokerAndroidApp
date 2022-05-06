package com.example.poker.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/***********************************************
 *  Room Database for player
 ***********************************************/

@Database(entities = {Player.class}, version = 4, exportSchema = true)
public abstract class PlayerDatabase extends RoomDatabase {
    public abstract PlayerDAO playerDAO();
}
