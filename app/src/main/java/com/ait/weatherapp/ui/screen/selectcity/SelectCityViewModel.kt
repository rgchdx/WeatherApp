package com.ait.weatherapp.ui.screen.selectcity

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.weatherapp.data.weatherResults
import com.ait.weatherapp.network.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SelectCityViewModel @Inject constructor(
    private val weatherApi: WeatherApi
) : ViewModel() {
    private val _addedCitiesWeather = mutableStateListOf<weatherResults>()
    val addedCitiesWeather: List<weatherResults> get() = _addedCitiesWeather

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun addCity(city: String) {
        if (_addedCitiesWeather.none { it.name == city }) {
            viewModelScope.launch {
                try {
                    val weatherData = weatherApi.getWeather(
                        city = city,
                        units = "imperial",
                        appid = "f3d694bc3e1d44c1ed5a97bd1120e8fe"
                    )
                    _addedCitiesWeather.add(weatherData)
                } catch (e: IOException) {
                    println("Network error while fetching weather: ${e.message}")
                } catch (e: HttpException) {
                    println("HTTP error while fetching weather: ${e.message}")
                }
            }
        }
    }
}