package com.example.scoot_scoot.android.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.AutocompleteSearchBox.AutocompleteSearchBox
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.Screens.MapScreen
import com.example.scoot_scoot.android.Screens.Screens
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.CameraPositionState

object MapUi {

    @Composable
    fun MapUi(
        navController: NavController,
        mvm: MapViewModel,
        fusedLocationClient: FusedLocationProviderClient,
        cameraState: CameraPositionState,
    ) {
        Box(

            modifier = Modifier.fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.Profile)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset((-20).dp, 20.dp)
                    .size(75.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    MapScreen.updateLocation(fusedLocationClient, cameraState)
                    mvm.useLocation.value = true;
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset((-20).dp, (-110).dp)
                    .size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_near_me_24),
                    contentDescription = "Profile",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }
            AutocompleteSearchBox(mvm, Modifier.align(Alignment.BottomCenter)) { }
        }
    }

}