package com.example.bookitdriver.pojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApI {
    @GET
    Call<String> getPath(@Url String url);



}
