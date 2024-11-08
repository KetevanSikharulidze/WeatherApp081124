package com.example.weatherapp081124.models

import java.sql.Time

data class DayItem(
    val city: String,
    val time: String,
    val tempCurrent: String,
    val condition: String,
    val conditionURL: String,
    val minTemp: String,
    val maxTemp: String,
    val hours: String
)
