package com.example.hw2

import android.accounts.AuthenticatorDescription
import android.content.res.Resources
import android.inputmethodservice.Keyboard
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import kotlin.math.round


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomNavigationView()
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text  ="City Trip",
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick ={
            navController.navigate("Screen2")
        },
        ){
            Text("Start")

        }

    }

}


@Composable
fun SecondScreen(navController: NavHostController){
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
                    CityRow(cityData = cityData)
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
//        modifier = Modifier

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
fun CustomNavigationView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Welcome") {
        composable(
            route = "Welcome",
        ) {
            WelcomeScreen(navController)
        }
        composable(
            route = "Screen2",
        ) {
            SecondScreen(navController)
        }
    }
}


data class CityData(
    val name: String,
    val description: String,
    val imageResourceName: Int
)

@Composable
fun CityRow(cityData: CityData) {

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
                    text = cityData.name,
                )
                Text(
                    text = cityData.description,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomNavigationView()

}