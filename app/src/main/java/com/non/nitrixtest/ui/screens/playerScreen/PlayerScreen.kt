package com.non.nitrixtest.ui.screens.playerScreen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.non.nitrixtest.R
import com.non.nitrixtest.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val movieList by mainViewModel.movieData.observeAsState(emptyList())
    val currentIndex by mainViewModel.currentIndex.observeAsState(0)
    var showControls by remember { mutableStateOf(true) }
    var isFullscreen by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var hideControlsJob by remember { mutableStateOf<Job?>(null) }
    var playbackPosition by rememberSaveable { mutableLongStateOf(0L) }
    var playWhenReady by rememberSaveable { mutableStateOf(true) }

    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    val window = activity.window
    val insetsController = window.insetsController

    val exoPlayer = mainViewModel.exoPlayer ?: run {
        mainViewModel.initExoPlayer(context)
        mainViewModel.exoPlayer!!
    }

    val currentMovie = remember(currentIndex) {
        movieList.getOrNull(currentIndex)
    }

    val hideControlsAfterDelay = {
        hideControlsJob?.cancel()
        hideControlsJob = coroutineScope.launch {
            delay(3000)
            showControls = false
        }
    }

    val playerView = remember { PlayerView(context) }.apply {
        player = exoPlayer
        useController = false
        if (Build.VERSION.SDK_INT >= 29) {
            this.transitionAlpha = 0.5f
        }
    }

    LaunchedEffect(currentMovie) {
        currentMovie?.sources?.firstOrNull()?.let { videoUrl ->
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.seekTo(playbackPosition)
            exoPlayer.playWhenReady = playWhenReady
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentPosition = exoPlayer.currentPosition
            duration = exoPlayer.duration.coerceAtLeast(0L)
            delay(1000)
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val newIndex = movieList.indexOfFirst { movie ->
                    movie.sources.firstOrNull() == mediaItem?.playbackProperties?.uri.toString()
                }
                if (newIndex != -1) {
                    mainViewModel.setCurrentIndex(newIndex)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            playbackPosition = exoPlayer.currentPosition
            playWhenReady = exoPlayer.playWhenReady
        }
    }

    BackHandler {
        playbackPosition = exoPlayer.currentPosition
        playWhenReady = exoPlayer.playWhenReady
        exoPlayer.stop()
        navController.popBackStack()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())

        insetsController?.systemBarsBehavior =
            android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        @Suppress("DEPRECATION")
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
                )
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            showControls = !showControls
                            if (showControls) {
                                hideControlsAfterDelay()
                            }
                        }
                    )
                }
        ) {


            IconButton(
                onClick = {
                    playbackPosition = exoPlayer.currentPosition
                    playWhenReady = exoPlayer.playWhenReady
                    exoPlayer.stop()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        if (Build.VERSION.SDK_INT >= 29) {
                            this.transitionAlpha = 0.5f
                        }
                    }
                }
            )

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { playerView }
            )

            if (showControls) {


                CustomPlayerControls(
                    player = exoPlayer,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp),
                    currentIndex = currentIndex,
                    mainViewModel = mainViewModel,
                    movieList = movieList,
                    onControlsInteraction = {
                        showControls = true
                        hideControlsAfterDelay()
                    }
                )
            }

            IconButton(
                onClick = {
                    isFullscreen = !isFullscreen

                    activity.requestedOrientation = if (isFullscreen) {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    painter = if (isFullscreen) painterResource(R.drawable.ic_fullscreen_small) else painterResource(
                        R.drawable.ic_fullscreen_big
                    ),
                    contentDescription = if (isFullscreen) "Exit Fullscreen" else "Enter Fullscreen",
                    tint = Color.LightGray
                )
            }
        }
    }
}

fun formatDuration(durationMs: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(durationMs)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}