package com.example.urlshortner.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlData {

    @SerializedName("result_url")
    @Expose
    String result_url;

    public String getResult_url() {
        return result_url;
    }

    public void setResult_url(String result_url) {
        this.result_url = result_url;
    }
}
