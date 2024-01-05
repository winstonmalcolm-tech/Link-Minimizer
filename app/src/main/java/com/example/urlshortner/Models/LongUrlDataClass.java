package com.example.urlshortner.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LongUrlDataClass {

    @SerializedName("url")
    @Expose
    private String url;
    public LongUrlDataClass(String url) {
        this.url = url;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
