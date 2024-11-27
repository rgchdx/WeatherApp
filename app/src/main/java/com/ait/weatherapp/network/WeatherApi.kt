package com.ait.weatherapp.network

import com.ait.weatherapp.data.Weather
import com.ait.weatherapp.data.weatherResults
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/
//data/2.5/weather
// ?q=Budapest,hu&units=imperial&
// appid=f3d694bc3e1d44c1ed5a97bd1120e8fe
//my appid = ffc20824d8e10e9fb16d779df56e7f55

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("units") units: String,
                           @Query("appid") appid: String): weatherResults

    suspend fun getWeatherIcon(@Query("icon") icon: String): Weather

}