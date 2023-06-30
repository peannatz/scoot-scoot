package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.common.MediaItem
import com.example.scoot_scoot.android.R
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Popup {
    @Composable
    fun Show(popupOpen: MutableState<Boolean>) {
        if (popupOpen.value) {
            val context = LocalContext.current
            val uri = "android.resource://" + context.packageName + "/" + R.raw.rickroll;
            val mediaItem = MediaItem.Builder().setUri(uri).build()
            val showCloseButton = remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    this.setMediaItem(mediaItem)
                    this.prepare()
                    this.playWhenReady = true
                    this.addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            if (isPlaying) {
                                coroutineScope.launch { waitUntilShowingCloseButton(showCloseButton) }
                            }
                        }
                    })
                }
            }


            Dialog(
                onDismissRequest = { /*TODO*/ },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    modifier = Modifier.background(color = MaterialTheme.colors.secondary),
                    elevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.32f)
                    ) {
                        AndroidView(factory = { context ->
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false
                                layoutParams =
                                    FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams
                                            .MATCH_PARENT,
                                        ViewGroup.LayoutParams
                                            .MATCH_PARENT
                                    )
                            }
                        })
                        if (showCloseButton.value) {
                            Button(
                                onClick = { popupOpen.value = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd).size(40.dp),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close, contentDescription = "",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private suspend fun waitUntilShowingCloseButton(isVisible: MutableState<Boolean>) {
        delay(5000)
        isVisible.value = true;
    }
}
