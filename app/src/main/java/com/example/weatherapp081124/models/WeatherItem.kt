package com.example.weatherapp081124.models

data class WeatherItem(
    val city: String,
    val time: String,
    val tempCurrent: String,
    val condition: String,
    val conditionURL: String,
    val minTemp: String,
    val maxTemp: String,
    val hours: String
)
