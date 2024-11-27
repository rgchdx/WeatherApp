package com.ait.weatherapp.di

import com.ait.weatherapp.network.WeatherApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherAPIHost
//https://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=imperial&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @WeatherAPIHost
    @Singleton
    fun provideWeatherAPIRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherAPI(@WeatherAPIHost retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}