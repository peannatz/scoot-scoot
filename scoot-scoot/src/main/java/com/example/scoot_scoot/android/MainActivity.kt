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
import com.example.scoot_scoot.android.Screens.Dashboard.Dashboard
import com.example.scoot_scoot.android.Screens.LoginScreen.LoginScreen
import com.example.scoot_scoot.android.Screens.MapScreen.MapScreen
import com.example.scoot_scoot.android.Screens.ProfileScreen.ProfileScreen
import com.example.scoot_scoot.android.Screens.RegisterScreen.RegisterScreen
import com.example.scoot_scoot.android.Screens.Screens
import com.example.scoot_scoot.android.Screens.SplashScreen.SplashScreen

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
        composable(Screens.Register) { RegisterScreen(navController) }
        composable(Screens.Login) { LoginScreen(navController) }
        composable(Screens.Profile) { ProfileScreen(navController) }
    }

}