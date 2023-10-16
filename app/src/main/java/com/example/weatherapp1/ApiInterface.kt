package com.example.weatherapp1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.StringBuilder

interface ApiInterface {
    @GET("weather")
    fun  getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ) : Call<weatherapp>

}