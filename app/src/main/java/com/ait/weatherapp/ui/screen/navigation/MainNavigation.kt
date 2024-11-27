package com.ait.weatherapp.ui.screen.navigation

sealed class Screen(val route: String ){
    object DisplayWeather : Screen("displayweather/budapest")
    object SearchCity : Screen("searchcity")
}