package com.example.hw2

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherData>()
    private val _currentWeatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> get() = _weatherData
    val currentWeatherData: LiveData<WeatherData> get() = _currentWeatherData

    fun fetchWeatherForLocation(
        context: Context,
        weatherApiService: WeatherApiService,
        apiKey: String
    ) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModelScope.launch {
                    var location = LocationProvider.getCurrentLocation()
                    if (location == null) {
                        delay(6000L)
                        location = LocationProvider.getCurrentLocation()
                    }
                    Log.e(ContentValues.TAG, "LOCATION BEFORE REQUEST" + location.toString())
                    if (location != null) {
                        val cityName = "${location.first},${location.second}"
                        val weather = weatherApiService.getWeather(cityName, apiKey)
                        _currentWeatherData.postValue(weather)
                    } else {
                        Log.e(ContentValues.TAG, "No last known location available")
                    }
                }
            } else {
                Log.e(ContentValues.TAG, "Location permission not granted")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error fetching weather data", e)
        }
    }






    fun fetchWeatherData(cityData: CityData) {
        Log.e(ContentValues.TAG, "fetchWeatherData")
        viewModelScope.launch {
            try {
                val apiKey = getApiKey()
                val weather = weatherApiService.getWeather(cityData.name, apiKey)
                _weatherData.postValue(weather)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.toString())
            }
        }
    }

    private fun getApiKey(): String {
        return "8e7826a07c1644d280d82050231311"
    }
}