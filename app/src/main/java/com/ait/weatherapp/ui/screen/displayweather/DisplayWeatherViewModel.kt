package com.ait.weatherapp.ui.screen.displayweather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.weatherapp.data.weatherResults
import com.ait.weatherapp.network.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface WeatherUiState {
    data class Success(val weatherResults: weatherResults) : WeatherUiState
    object Init: WeatherUiState
    object Error : WeatherUiState
    object Loading : WeatherUiState
}


@HiltViewModel
class DisplayWeatherViewModel @Inject constructor(
    val weatherApi: WeatherApi
) : ViewModel() {
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Init)

    fun getWeather(city: String, units: String, appid: String = "f3d694bc3e1d44c1ed5a97bd1120e8fe") {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
             weatherUiState= try {
                val result = weatherApi.getWeather(
                    city,
                    units,
                    "f3d694bc3e1d44c1ed5a97bd1120e8fe"
                )
                WeatherUiState.Success(result)
            } catch (e: IOException) {
                WeatherUiState.Error
            } catch (e: HttpException) {
                WeatherUiState.Error
            }
        }
    }
}