package com.non.nitrixtest.ui.screens.playerScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.non.nitrixtest.R
import com.non.nitrixtest.data.entities.Movie
import com.non.nitrixtest.viewmodel.MainViewModel

@Composable
fun CustomPlayerControls(
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    currentIndex: Int,
    mainViewModel: MainViewModel,
    movieList: List<Movie>,
    onControlsInteraction: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(player.isPlaying) }
    var currentPosition by remember { mutableLongStateOf(player.currentPosition) }
    var duration by remember { mutableLongStateOf(player.duration) }

    LaunchedEffect(Unit) {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                currentPosition = newPosition.positionMs
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY && player.duration > 0) {
                    duration = player.duration
                }
            }
        })
    }

    LaunchedEffect(player, isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            kotlinx.coroutines.delay(500L)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        val validDuration = if (duration > 0) duration else 0L
        val validPosition = if (currentPosition <= validDuration) currentPosition else 0L

        Text(
            text = "${formatDuration(currentPosition)} / ${formatDuration(duration)}",
            color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            value = validPosition.toFloat(),
            onValueChange = {
                currentPosition = it.toLong()
                player.seekTo(currentPosition)
            },
            onValueChangeFinished = {
                onControlsInteraction()
            },
            valueRange = 0f..validDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.LightGray,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentIndex > 0) {
                        mainViewModel.setCurrentIndex(currentIndex - 1)
                    }
                    onControlsInteraction()
                },
                enabled = currentIndex > 0
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_previous),
                    contentDescription = "Previous",
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = {
                    if (isPlaying) player.pause() else player.play()
                    onControlsInteraction()
                }
            ) {
                Icon(
                    if (isPlaying) painterResource(R.drawable.ic_pause) else painterResource(R.drawable.ic_play),
                    contentDescription = if (isPlaying) "Pause" else "Play",  modifier = Modifier.padding(5.dp), tint = Color.LightGray
                )
            }

            IconButton(
                onClick = {
                    if (currentIndex < movieList.size - 1) {
                        mainViewModel.setCurrentIndex(currentIndex + 1)
                    }
                    onControlsInteraction()
                },
                enabled = currentIndex < movieList.size - 1
            ) {
                Icon(painter = painterResource(R.drawable.ic_next), contentDescription = "Next", tint = Color.LightGray)
            }
        }
    }
}


