package com.example.scoot_scoot.android

import android.os.Bundle
import android.transition.Scene
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoot_scoot.android.Dashboard.Dashboard
import com.example.scoot_scoot.android.Map.Map
import com.example.scoot_scoot.android.SplashScreen.SplashScreen
import com.google.maps.android.compose.GoogleMap

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Splash) {
        composable(Screens.Splash) {
            SplashScreen(navController)
        }
        composable(Screens.Dashboard) { Dashboard(navController) }
        composable(Screens.Map) { Map(navController) }
    }

}

@Composable
fun GreetingView(text: String) {
    val contextForToast = LocalContext.current.applicationContext
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleMap(modifier = Modifier.fillMaxHeight(0.6f))
        Text(text = text)
        Button(onClick = {
            Toast.makeText(contextForToast, "Ouch!", Toast.LENGTH_SHORT).show()
        })
        { Text(text = "Press me") }
    }
}

@Preview
@Composable
fun AddText() {
    Column {
        Text("ouch")
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
