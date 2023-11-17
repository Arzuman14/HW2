package com.example.hw2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val TAG = "WEATHER"
class MainActivity : ComponentActivity() {
    private val locationPermissionRequestCode = 123

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            requestLocationPermission()
            LocationProvider.initialize(this)

            NavHost(navController = navController, startDestination = "Welcome") {
                val viewModel = WeatherViewModel()
                viewModel.fetchWeatherForLocation(
                    this@MainActivity,
                    weatherApiService,
                    "8e7826a07c1644d280d82050231311"
                )
                composable(
                    route = "Welcome",
                ) {
                    WelcomeScreen(navController, viewModel)
                }
                composable(
                    route = "Screen2",
                ) {
                    SecondScreen(navController, viewModel)
                }
            }
        }
    }


    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                print("nnnnnnnnnnnnnnnnnnnnnooooooooooooooooooo*******************")
            }
        }
    }
}



val loggingInterceptor = HttpLoggingInterceptor().also {
    it.level = HttpLoggingInterceptor.Level.BODY
}
val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.weatherapi.com/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface WeatherApiService {
    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("q") name: String,
        @Query("key") apiKey: String
    ): WeatherData
}


val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)



typealias LocationCoordinate = Pair<Double, Double>

object LocationProvider {
    private var currentLocation: LocationCoordinate? = null
    fun getCurrentLocation(): LocationCoordinate? {
        return currentLocation
    }

    @SuppressLint("MissingPermission")
    fun initialize(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5f) {
            it.longitude = 180 + it.longitude
            currentLocation = it.longitude to it.latitude

            Log.e(TAG, "initialize: $currentLocation")
        }
    }
}
