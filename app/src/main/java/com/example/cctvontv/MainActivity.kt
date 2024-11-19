package com.example.cctvontv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@UnstableApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            // Initialize ExoPlayer
            val player = remember {
                ExoPlayer.Builder(context)
                    .setRenderersFactory{ context, extensionRendererMode, mediaCodecSelector ->
                        ExoPlayer.DefaultRenderersFactory(context)
                            .apply { setExtensionRendererMode(extensionRendererMode) }
                    }
                    .build()
            }

            // RTSP URL
            val rtspUrl = "rtsp://example.com/stream" // Replace with your RTSP stream URL

            // Configure ExoPlayer
            DisposableEffect(player) {
                val mediaItem = androidx.media3.common.MediaItem.fromUri(rtspUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true

                onDispose {
                    player.release()
                }
            }

            // Display ExoPlayer in Compose
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    PlayerView(it).apply {
                        this.player = player
                        this.useController = false // Optional: Enable or disable controls
                    }
                }
            )
        }
    }
}
