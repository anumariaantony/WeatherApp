package com.example.weatherapp.repository

import com.example.weatherapp.model.CurrentWeatherDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?exclude=minutely,hourly,daily,alerts&units=metric")
    suspend fun getWeatherDetails(@Query("lat") latitude :Double,
                                  @Query("lon") longitude :Double,
                                  @Query("appid") apiKey : String
    ) : Response<CurrentWeatherDetails>
}