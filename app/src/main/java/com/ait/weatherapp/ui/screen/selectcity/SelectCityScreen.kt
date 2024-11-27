package com.ait.weatherapp.ui.screen.selectcity

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ait.weatherapp.R
import com.ait.weatherapp.data.Weather
import com.ait.weatherapp.data.weatherResults
import com.ait.weatherapp.ui.screen.displayweather.DisplayWeatherViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCityScreen(
    viewModel: SelectCityViewModel = hiltViewModel(),
    displayWeatherViewModel: DisplayWeatherViewModel,
    navController: NavController
) {
    val cities = listOf(
        stringResource(R.string.tokyo),
        stringResource(R.string.delhi),
        stringResource(R.string.shanghai), stringResource(R.string.s_o_paulo),
        stringResource(R.string.mexico_city),
        stringResource(R.string.cairo),
        stringResource(R.string.mumbai),
        stringResource(R.string.beijing),
        stringResource(R.string.dhaka), stringResource(R.string.osaka),
        stringResource(R.string.new_york), stringResource(R.string.karachi)
    )
    val addedCitiesWeather = viewModel.addedCitiesWeather
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Select City") }
    var addedCities by remember { mutableStateOf(emptyList<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.select_a_city),
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(
                        onClick = { expanded = !expanded },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_city),
                                tint = Color.White
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF001F54),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) {
        innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{
                if(expanded){
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        TextField(
                            value = selectedCity,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.select_city)) },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(
                                    R.string.dropdown_icon
                                )
                                )
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            cities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        selectedCity = city
                                        expanded = false
                                        viewModel.addCity(city)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            items(addedCitiesWeather.size) { index ->
                val cityWeather = addedCitiesWeather[index]
                ResultCard(
                    weatherResults = cityWeather,
                    weather = cityWeather.weather?.getOrNull(0) ?: Weather(),
                    navController = navController,
                    city = cityWeather.name ?: stringResource(R.string.unknown)
                )
            }

        }
    }
}

@Composable
fun ResultCard(
    weatherResults: weatherResults,
    weather: Weather,
    navController: NavController,
    city: String){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${weather.icon}@2x.png"),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = stringResource(
                    R.string.city,
                    weatherResults.name ?: stringResource(R.string.unknown)
                ),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(
                    R.string.description,
                    weatherResults.weather?.getOrNull(0)?.description ?: stringResource(
                        R.string.no_description_available
                    )
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(
                    R.string.temperature,
                    weatherResults.main?.temp?.toString() ?: stringResource(R.string.no_temperature_available)
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = {
                    navController.navigate("displayweather/$city")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.view_weather))
            }
        }
    }
}



