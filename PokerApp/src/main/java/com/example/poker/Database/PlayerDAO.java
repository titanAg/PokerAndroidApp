package com.example.poker.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/***********************************************
 * Data Access Object for Player Table
 *      - Maintains a single player row to allow storing of chips
 *        and possibly additional information
 ***********************************************/

@Dao
public interface PlayerDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addPlayer(Player user);

    @Query("select * from players where id = 1")
    public Player getPlayer();

    @Update
    public void updatePlayer(Player user);

    @Delete
    public void deletePlayer(Player user);

}
