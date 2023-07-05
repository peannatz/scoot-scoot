package com.example.scoot_scoot.android.Components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.R

object ProfileBottomBar {
    @Composable
    fun ProfileBottomBar(navController: NavController) {
        println(navController.context)
        BottomNavigation(
            modifier = Modifier.fillMaxHeight(),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            },
                label = { Text(text = "Balance") }
            )
            BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            },
                label = { Text(text = "Trips") }
            )
            BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_map_24),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            },
                label = { Text(text = "Trips") }
            )
            BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            },
                label = { Text(text = "Account") }
            )
            BottomNavigationItem(selected = false, onClick = { /*TODO*/ }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            },
                label = { Text(text = "Help") }
            )
        }

    }
}