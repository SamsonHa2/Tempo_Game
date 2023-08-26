package com.example.rememberthebeat.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
    val audioSource = MediaItem.fromUri("android.resource://${context}/${R.raw.ogg120trimmed}")
    exoPlayer.apply {
        setMediaItem(audioSource)
        repeatMode = Player.REPEAT_MODE_ONE
        prepare()
        play()
    }
    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
            exoPlayer.pause()
        }
        else if (lifecycleEvent == Lifecycle.Event.ON_STOP) {
            exoPlayer.pause()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val clickCount by viewModel.clickCount.collectAsState()
        val textT:String = when {
            clickCount < 4 -> "tap along to the beat"
            clickCount in 4..11 -> "that's great! now we'll see how well you can play on your own."
            clickCount in 12..15 -> "the backing track is going to fade out"
            clickCount == 16 -> "the backing track is going to fade out\n5"
            clickCount == 17 -> "the backing track is going to fade out\n4"
            clickCount == 18 -> "the backing track is going to fade out\n3"
            clickCount == 19 -> "the backing track is going to fade out\n2"
            clickCount == 20 -> "the backing track is going to fade out\n1"
            clickCount == 35 -> "the backing track is going to fade out\n5"
            clickCount == 36 -> "the backing track is going to fade out\n4"
            clickCount == 37 -> "the backing track is going to fade out\n3"
            clickCount == 38 -> "the backing track is going to fade out\n2"
            clickCount == 39 -> "the backing track is going to fade out\n1"
            else -> ""
        }
        val vol:Float = when {
            clickCount < 16 -> 1F
            clickCount == 16 -> 0.8F
            clickCount == 17 -> 0.6F
            clickCount == 18 -> 0.4F
            clickCount == 19 -> 0.2F
            else -> 0F
        }

        Button(
            onClick = {
                viewModel.incrementClickCount()
                exoPlayer.volume = 1F//vol
                if (clickCount==20){
                    viewModel.setStart(System.currentTimeMillis())
                }
                if (clickCount in 21..40){
                    viewModel.addClickTime(System.currentTimeMillis())
                }
            },
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape) {
        }
        Text(text = textT)
        if (clickCount == 41){
            viewModel.calculateScores()
        }
        if (clickCount > 39){
            Text(text = viewModel.scores.toString())
        }
    }
}

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    RememberTheBeatTheme {
        GameScreen(navController = rememberNavController())
    }
}