package com.example.rememberthebeat.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.math.abs

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
    ){
    if (viewModel.state.value == "game"){
        val context = LocalContext.current
        val exoPlayer = remember { ExoPlayer.Builder(context).build() }
        val audioSource = MediaItem.fromUri("android.resource://${context}/${R.raw.ogg120trimmed}")
        exoPlayer.apply {
            setMediaItem(audioSource)
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
            play()
        }
        val lifecycleEvent = rememberLifecycleEvent()
        LaunchedEffect(lifecycleEvent) {
            when (lifecycleEvent) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.stop()
                }
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.stop()
                }
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.play()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.stop()
                    exoPlayer.release()
                }
                else -> {}
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Button(
                onClick = {
                    viewModel.incrementClickCount()
                    viewModel.updateGameState()
                    exoPlayer.volume = viewModel.getGameVolume()
                },
                modifier = Modifier.fillMaxSize(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
            ) {
                Text(text = viewModel.getGameText()+"\n\n\n\n\n\n\n\n\n\n", fontSize = 24.sp, lineHeight = 28.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
    else if (viewModel.state.value == "summary"){
        Summary(
            scores = viewModel.scores,
            totalScore = viewModel.getTotalScore(),
            highScore = viewModel.getHighScore(),
            navController = navController
        )
    }

}

@Composable
fun Summary(
    scores: List<Int>,
    totalScore: Int,
    highScore: Int,
    navController: NavController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ){

        Column(
            modifier = Modifier
                .background(Color.Cyan)
                .align(Alignment.Center)
                .padding(36.dp)
        ) {
            Row {
                Text(
                    text = "$totalScore\nYour Score",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1F)
                )

                Spacer(
                    modifier = Modifier
                        .size(1.dp, 55.dp)
                        .background(Color.Black)
                )
                Text(
                    text = "$highScore\nHigh Score",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1F)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Here's how your score breaks down:",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(48.dp))
            Row{
                Text(
                    text = "\nToo Early",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = "Perfect Timing",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = "\nToo Late",
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1F)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                ) {
                    for (i in scores){
                        Spacer(modifier = Modifier.size(0.dp, 5.dp))
                        if (i < 0){
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(abs(i) / 50F)
                                    .height(3.dp)
                                    .background(Color.Black)
                                    .align(Alignment.End)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                        Spacer(modifier = Modifier.size(0.dp, 5.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                ) {
                    for (i in scores){
                        Spacer(modifier = Modifier.size(0.dp, 5.dp))
                        if (i > 0){
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth(abs(i) / 50F)
                                    .height(3.dp)
                                    .background(Color.Black)
                                    .align(Alignment.Start)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                        Spacer(modifier = Modifier.size(0.dp, 5.dp))
                    }
                }
            }


            Row{
                Text(
                    text = "0 pts",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = "50 pts",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = "0 pts",
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1F)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(onClick = { navController.navigate("start_screen") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Play Again")
            }
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