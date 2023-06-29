package com.example.scoot_scoot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoot_scoot.android.Dashboard.Dashboard
import com.example.scoot_scoot.android.MapScreen.MapScreen
import com.example.scoot_scoot.android.SplashScreen.SplashScreen

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
        composable(Screens.Map) { MapScreen(navController) }
        composable(Screens.Register) { MapScreen(navController) }
        composable(Screens.Login) { MapScreen(navController) }
        composable(Screens.Profile) { MapScreen(navController) }
    }

}