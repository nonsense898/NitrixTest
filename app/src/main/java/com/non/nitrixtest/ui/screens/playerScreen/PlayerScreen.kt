package com.non.nitrixtest.ui.screens.playerScreen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.non.nitrixtest.R
import com.non.nitrixtest.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val window = activity.window
    val insetsController = window.insetsController

    val exoPlayer = mainViewModel.exoPlayer ?: run {
        mainViewModel.initExoPlayer(context)
        mainViewModel.exoPlayer!!
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

    LaunchedEffect(Unit) {
        val currentMovie = mainViewModel.movieData.value?.getOrNull(mainViewModel.currentIndex.value ?: 0)
        currentMovie?.sources?.firstOrNull()?.let { videoUrl ->
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.seekTo(playbackPosition)
            exoPlayer.playWhenReady = playWhenReady
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            playbackPosition = exoPlayer.currentPosition
            playWhenReady = exoPlayer.playWhenReady
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())

        insetsController?.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
                .fillMaxHeight(1f )
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
                    exoPlayer.release()
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
                    painter = if (isFullscreen) painterResource(R.drawable.ic_fullscreen_small) else painterResource(R.drawable.ic_fullscreen_big),
                    contentDescription = if (isFullscreen) "Exit Fullscreen" else "Enter Fullscreen",
                    tint = Color.LightGray
                )
            }
        }
    }
}