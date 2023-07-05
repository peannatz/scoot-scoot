package com.example.scoot_scoot.android.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.scoot_scoot.android.BuildConfig
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.IOException

object AutocompleteSearchBox {

    var placesClient: PlacesClient? = null

    @Composable
    fun AutocompleteSearchBox(
        mvm: MapViewModel,
        modifier: Modifier = Modifier,
        onTextChange: () -> Unit
    ) {

        val context = LocalContext.current

        if (placesClient == null) {
            try {
                Places.initialize(context, BuildConfig.MAPS_API_KEY)
                placesClient = Places.createClient(context)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        var destinationText =
            remember { mutableStateOf(TextFieldValue(mvm.destination.value.address)) }

        AnimatedVisibility(
            mvm.locationAutofill.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .padding(8.dp)
                .offset(y = (-80).dp)
                .run { modifier }
                .background(MaterialTheme.colors.secondary)
        )
        {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                itemsIndexed(mvm.locationAutofill) { _, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                mvm.destination.value = item
                                destinationText.value = TextFieldValue(item.address)
                                onTextChange()
                                mvm.locationAutofill.clear()
                            }
                    ) {
                        Text(item.address)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }


        TextField(
            value = destinationText.value,
            onValueChange =
            {
                destinationText.value = it
                mvm.destination.value.address = it.text
                mvm.searchPlaces(it.text, placesClient!!)
            }, Modifier
                .fillMaxWidth(0.8f)
                .run { modifier }
                .offset(y = (-30).dp),
            placeholder = { Text("Destination") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background
            )
        )
    }
}