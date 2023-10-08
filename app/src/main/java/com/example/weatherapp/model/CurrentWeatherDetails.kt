package com.example.weatherapp.model

data class CurrentWeatherDetails(
    val current: Current,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)