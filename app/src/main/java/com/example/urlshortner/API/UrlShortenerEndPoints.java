package com.example.urlshortner.API;

import com.example.urlshortner.Models.LongUrlDataClass;
import com.example.urlshortner.Models.UrlData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UrlShortenerEndPoints {

    @FormUrlEncoded
    @Headers({
            "content-type: application/x-www-form-urlencoded",
            "X-RapidAPI-Key: ",
            "X-RapidAPI-Host: url-shortener-service.p.rapidapi.com",
    })
    @POST("shorten")
    Call<UrlData> getShortenUrl(@Field("url") String url);
}
