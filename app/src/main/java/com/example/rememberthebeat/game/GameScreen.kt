package com.example.rememberthebeat.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.ui.theme.RememberTheBeatTheme
import com.example.rememberthebeat.R

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
    ){
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    val audioSource = MediaItem.fromUri("android.resource://${context}/${R.raw.ogg120}")
    exoPlayer.apply {
        setMediaItem(audioSource)
        repeatMode = Player.REPEAT_MODE_ONE
        prepare()
        play()
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {  }, modifier = Modifier.fillMaxSize(), shape = RectangleShape) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    RememberTheBeatTheme {
        GameScreen(navController = rememberNavController())
    }
}