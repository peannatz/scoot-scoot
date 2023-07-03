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
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Screens.AlcoholTest.AlcoholTest
import com.example.scoot_scoot.android.Screens.Dashboard.Dashboard
import com.example.scoot_scoot.android.Screens.LoginScreen.LoginScreen
import com.example.scoot_scoot.android.Screens.MapScreen.MapScreen
import com.example.scoot_scoot.android.Screens.PermissionsScreen.PermissionsScreen
import com.example.scoot_scoot.android.Screens.ProfileScreen.ProfileScreen
import com.example.scoot_scoot.android.Screens.RegisterScreen.RegisterScreen
import com.example.scoot_scoot.android.Screens.Screens
import com.example.scoot_scoot.android.Screens.SplashScreen.SplashScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        UserManager.init(this)
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

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Map) {
        composable(Screens.Splash) {
            SplashScreen(navController)
        }
        composable(Screens.Dashboard) { Dashboard(navController) }
        composable(Screens.Map) { MapScreen(navController) }
        composable(Screens.Register) { RegisterScreen(navController) }
        composable(Screens.Login) { LoginScreen(navController) }
        composable(Screens.Profile) { ProfileScreen(navController) }
        composable(Screens.AlcTest) { AlcoholTest(navController) }
        composable(Screens.Permissions) { PermissionsScreen(navController) }
    }

}