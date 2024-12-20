package com.example.weatherapp081124

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp081124.models.WeatherItem

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherItem>()
    val liveDataList = MutableLiveData<List<WeatherItem>>()
}