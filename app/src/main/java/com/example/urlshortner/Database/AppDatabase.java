package com.example.urlshortner.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.urlshortner.Dao.UrlDao;
import com.example.urlshortner.Entities.Url;

@Database(entities = {Url.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UrlDao urlDao();
}
