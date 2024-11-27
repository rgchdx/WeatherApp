package com.ait.weatherapp.ui.screen.displayweather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ait.weatherapp.R
import com.ait.weatherapp.data.Weather
import com.ait.weatherapp.data.weatherResults
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun DisplayWeatherScreen(
    displayWeatherViewModel: DisplayWeatherViewModel = hiltViewModel(),
    cityName: String,
    onNavigateToSelectCity: () -> Unit
){
    var city = remember { mutableStateOf("") }
    var units = remember { mutableStateOf("")}
    LaunchedEffect(cityName){
        displayWeatherViewModel.getWeather(cityName,"imperial")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F54))
    ){
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToSelectCity,
                    containerColor = Color(0xFF001F54),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_city),
                            tint = Color.White
                        )
                    }
                )
            }
        ){ innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .background(Color(0xFF001F54))
            ){
                item {
                    Text(text = stringResource(R.string.display_weather),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp))
                }
                item {
                    when (val uiState = displayWeatherViewModel.weatherUiState) {
                        is WeatherUiState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                Text(text = stringResource(R.string.loading), style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        is WeatherUiState.Success -> {
                            ResultCard(
                                weatherResults = uiState.weatherResults,
                                weather = uiState.weatherResults.weather?.getOrNull(0) ?: Weather(),
                                onRefreshWeather = { city ->
                                    displayWeatherViewModel.getWeather(city, "imperial")
                                }
                            )
                        }

                        is WeatherUiState.Error -> {
                            Text(
                                text = stringResource(R.string.error_fetching_weather_data_please_try_again),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        WeatherUiState.Init -> {
                            Text(
                                text = stringResource(R.string.initializing),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ResultCard(
    weatherResults: weatherResults,
    weather: Weather,
    onRefreshWeather: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${weather.icon}@2x.png"),
                contentDescription = null,
                modifier = Modifier.size(360.dp)
            )

            Text(
                text = stringResource(
                    R.string.city,
                    weatherResults.name ?: stringResource(R.string.unknown)
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(
                    R.string.description,
                    weatherResults.weather?.getOrNull(0)?.description ?: stringResource(
                        R.string.no_description_available
                    )
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(
                    R.string.temperature,
                    weatherResults.main?.temp?.toString() ?: stringResource(R.string.no_temperature_available)
                ),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(
                    R.string.humidity,
                    weatherResults.main?.humidity?.toString() ?: stringResource(R.string.no_humidity_available)
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.wind_speed,
                    weatherResults.wind?.speed?.toString() ?: stringResource(R.string.no_wind_speed_available)
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.pressure,
                    weatherResults.main?.pressure?.toString() ?: stringResource(R.string.no_pressure_available)
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.visibility,
                    weatherResults.visibility?.toString() ?: stringResource(R.string.no_visibility_available)
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.cloudiness,
                    weatherResults.clouds?.all?.toString() ?: stringResource(R.string.no_cloudiness_available)
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { onRefreshWeather(weatherResults.name ?: "Unknown") },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(R.string.refresh_weather))
            }
        }
    }
}

