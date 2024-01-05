package com.example.urlshortner.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Url {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo()
    public String title;

    @ColumnInfo
    public String url;

    public Url(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
