package com.example.scoot_scoot.android.Screens

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Data.UserManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

object PermissionsScreen {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionsScreen(navController: NavController) {

        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        //TODO: wenn man alle permissions revoked bleibt man hängen :'(

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (locationPermissionsState.allPermissionsGranted) {
                Text("Thank you. I assure you it wasn't a mistake. Your data will be in good hands. They like touching data.")
                UserManager.savePermissionsStatus(true)
                navController.navigate(Screens.Map)
            } else {
                val allPermissionsRevoked =
                    locationPermissionsState.permissions.size ==
                            locationPermissionsState.revokedPermissions.size

                val textToShow = if (!allPermissionsRevoked) {
                    // If not all the permissions are revoked, it's because the user accepted the COARSE
                    // location permission, but not the FINE one.
                    "Wow! You've allowed me to access your approximate location.\n " +
                            "But imagine the thrill of scooting when we know your exact coordinates " +
                            "at all times.\n " +
                            "Only then can we fully unleash the power of scooting!\n " +
                            "Accept the fine location permission, and let's embark on this wild adventure together!"

                } else if (locationPermissionsState.shouldShowRationale) {
                    // Both location permissions have been denied
                    "Getting your exact location is important for this app. And our Stakeholders. And Google." +
                            "Please grant us fine location so we can feed our families. Thank you :)"
                } else {
                    "Behold, mortal! \n" +
                            "This sacred feature demands the sacred location permission. \n" +
                            "With it, you shall unlock the secrets of scooting. \n" +
                            "Grant us the permission now, or forever live in the mundane realm of pedestrianism!"
                }

                val buttonText = if (!allPermissionsRevoked) {
                    "Allow precise location"
                } else {
                    "Summon the permissions panel!"
                }

                Text(text = textToShow, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                    Text(buttonText)
                }
            }
        }
    }
}