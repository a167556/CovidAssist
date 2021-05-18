package com.example.fyp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiCall {

    @GET("v2/everything?q=covid-19%20AND%20malaysia%20AND%20new%20cases&apiKey=17312d078d7a465083f3df40d2d37b4c&domains=thestar.com.my&sortBy=publishedAt")
    Call<NewsData> getData();
}
