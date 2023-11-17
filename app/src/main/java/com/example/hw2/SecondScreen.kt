package com.example.hw2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController



@Composable
fun SecondScreen(navController: NavHostController, viewModel: WeatherViewModel){
    val cityList = listOf(
        CityData("New York " ,"The Big apple", R.drawable.new_york),
        CityData("Los Angeles" , "The City Of Angle", R.drawable.los_angeles),
        CityData("Chicago" , "The Wind City",R.drawable.chicago),
        CityData("Miami" , "The Magic City",R.drawable.miami),
        CityData("San Francisco" , "The Golden City", R.drawable.san_francisco),
        CityData("Rotterdam",  "Stronger trough effort", R.drawable.rotterdam),
        CityData("Boston" , "Beantown", R.drawable.boston),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)

    ){
        LazyColumn {
            items(cityList.chunked(3)){chunkOfCities ->
                Column(
                    modifier = Modifier.background(color = Color.Transparent),
                ){
                    chunkOfCities.forEach{cityData ->
                        CityRow(cityData = cityData, viewModel )
                        Divider()
                    }
                }
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End

    ){
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick ={
            navController.navigate("Welcome")
        },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)

        ){
            Text("Home")

        }
    }
}


@Composable
fun CityRow(cityData: CityData, viewModel:WeatherViewModel) {
//    var temperature by remember { mutableStateOf<Double?>(null) }
    val weatherData = viewModel.weatherData.observeAsState()
//     viewModel.fetchWeatherData(cityData){ temp ->
//         temperature=temp
//    }
    viewModel.fetchWeatherData(cityData)
    Card (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = cityData.imageResourceName),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = cityData.name
                )
                weatherData.value?.let {
                    Text(
                        text = "Temperature: ${it.current.temp_c}°C"
                    )
                }
                Text(
                    text = cityData.description,
                )
            }
        }
    }
}