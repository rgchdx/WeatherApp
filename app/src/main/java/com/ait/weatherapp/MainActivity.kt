package com.ait.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.weatherapp.network.WeatherApi
import com.ait.weatherapp.ui.screen.displayweather.DisplayWeatherScreen
import com.ait.weatherapp.ui.screen.displayweather.DisplayWeatherViewModel
import com.ait.weatherapp.ui.screen.navigation.Screen
import com.ait.weatherapp.ui.screen.selectcity.SelectCityScreen
import com.ait.weatherapp.ui.screen.selectcity.SelectCityViewModel
import com.ait.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "selectCity"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("displayWeather/{cityName}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: "Budapest"
            DisplayWeatherScreen(
                cityName = cityName,
                onNavigateToSelectCity = {
                    navController.navigate("selectCity")
                }
            )
        }
        composable("selectCity") {
            val selectCityViewModel: SelectCityViewModel = hiltViewModel()
            val displayWeatherViewModel: DisplayWeatherViewModel = hiltViewModel()

            SelectCityScreen(
                viewModel = selectCityViewModel,
                displayWeatherViewModel = displayWeatherViewModel,
                navController = navController
            )
        }
    }
}

