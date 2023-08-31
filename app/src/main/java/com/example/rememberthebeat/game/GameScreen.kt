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
    val game = remember { mutableStateOf(true) }
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
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
            exoPlayer.stop()
        }
        else if (lifecycleEvent == Lifecycle.Event.ON_STOP) {
            exoPlayer.stop()
        }
        else if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            exoPlayer.play()
        }
        else if (lifecycleEvent == Lifecycle.Event.ON_DESTROY){
            exoPlayer.stop()
            exoPlayer.release()
        }
    }
    if (game.value){
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val clickCount by viewModel.clickCount.collectAsState()
            val textT:String = when {
                clickCount < 4 -> "tap along to the beat"
                clickCount in 4..11 -> "that's great!\n\nnow we'll see how well you can play on your own."
                clickCount in 12..15 -> "the backing track is going to fade out"
                clickCount == 16 -> "the backing track is going to fade out\n\n5"
                clickCount == 17 -> "the backing track is going to fade out\n\n4"
                clickCount == 18 -> "the backing track is going to fade out\n\n3"
                clickCount == 19 -> "the backing track is going to fade out\n\n2"
                clickCount == 20 -> "the backing track is going to fade out\n\n1"
                clickCount in 21..34 -> "keep playing!"
                clickCount == 35 -> "nearly there! you can stop in\n\n5"
                clickCount == 36 -> "nearly there! you can stop in\n\n4"
                clickCount == 37 -> "nearly there! you can stop in\n\n3"
                clickCount == 38 -> "nearly there! you can stop in\n\n2"
                clickCount == 39 -> "nearly there! you can stop in\n\n1"
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
                    exoPlayer.volume = vol
                    if (clickCount==20){
                        viewModel.setStart(System.currentTimeMillis())
                    }
                    if (clickCount in 21..40){
                        viewModel.addClickTime(System.currentTimeMillis())
                    }
                },
                modifier = Modifier.fillMaxSize(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
            ) {
                Text(text = textT+"\n\n\n\n\n\n\n\n\n\n", fontSize = 24.sp, lineHeight = 28.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(30.dp))
            }
            if (clickCount == 40 && viewModel.trigger){
                viewModel.flip()
                viewModel.calculateScores()
                game.value = false
            }
        }
    }
    else{
        Summary(scores = viewModel.scores, navController = navController)
    }

}

@Composable
fun Summary(
    scores: List<Int>,
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
                    text = scores.sumOf{ 50 - abs(it) }.toString() + "\nYour Score",
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
                    text = scores.sumOf{ 50 - abs(it) }.toString() + "\nHigh Score",
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