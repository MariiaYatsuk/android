package com.example.apitest;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DB entity);

    @Query("SELECT * FROM phone_validation ORDER BY id DESC")
    List<DB> getAll();

    @Query("SELECT * FROM phone_validation ORDER BY id DESC")
    LiveData<List<DB>> getAllLiveData();

    @Insert
    void insertAll(List<DB> entities);
}
