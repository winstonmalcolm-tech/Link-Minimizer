package com.example.urlshortner.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.urlshortner.Entities.Url;

import java.util.List;

@Dao
public interface UrlDao {
    @Query("SELECT * FROM url")
    List<Url> getAll();

    @Insert
    long insertUrl(Url url);

    @Delete
    void delete(Url url);
}
