package com.example.intento1

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @POST("weather")
    suspend fun postWeatherData(
        @Body weatherData: WeatherData,
        @Query("appid") apiKey: String
    ): retrofit2.Response<Void>
}



