package com.example.rememberthebeat.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import com.example.rememberthebeat.data.models.Shape
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
            var shapes by remember { mutableStateOf(emptyList<Shape>()) }
            val shapeList = listOf("circle", "triangle", "square", "pentagon", "hexagon")
            val colorList = listOf(Color.Blue, Color.Magenta, Color.Yellow, Color.Green, Color.Red)
            val horizontalOffset =  Random.nextFloat() * 1000
            val verticalOffset =  Random.nextFloat() * 2000
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan)
                    .clickable {
                        shapes += Shape(
                            type = shapeList[Random.nextInt(0, shapeList.size)],
                            horizontalOffset = horizontalOffset,
                            verticalOffset = verticalOffset,
                            color = colorList[Random.nextInt(0, colorList.size)]
                        )
                    viewModel.incrementClickCount()
                    viewModel.updateGameState()
                    exoPlayer.volume = viewModel.getGameVolume()
                }
            ) {
                for (shape in shapes) {
                    when (shape.type) {
                        "circle" -> DrawCircleShape(shape)
                        "triangle" -> DrawTriangleShape(shape)
                        "square" -> DrawSquareShape(shape)
                        "pentagon" -> DrawPentagonShape(shape)
                        "hexagon" -> DrawHexagonShape(shape)
                    }
                }
                Text(
                    text = viewModel.getGameText()+"\n\n\n\n\n\n\n\n\n\n",
                    color = Color.Black,
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 48.dp)
                )
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

@Composable
private fun DrawCircleShape(circle:Shape) {
    var updatedVisible by remember { mutableStateOf(circle.visible) }

    val shapeSize by animateFloatAsState(
        targetValue = if (updatedVisible) 0F else 1F, label = "",
        animationSpec = tween(1000)
    )
    LaunchedEffect(circle) {
        updatedVisible = true
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .scale(shapeSize)
    ) {
        drawCircle(
            color = circle.color,
            center = Offset(circle.horizontalOffset, circle.verticalOffset),
            radius = 450F / 2
        )
    }
}

@Composable
private fun DrawTriangleShape(triangle:Shape) {
    var updatedVisible by remember { mutableStateOf(triangle.visible) }

    val shapeSize by animateFloatAsState(
        targetValue = if (updatedVisible) 0F else 1F, label = "",
        animationSpec = tween(1000)
    )

    val size = 450F * shapeSize

    LaunchedEffect(triangle) {
        updatedVisible = true
    }
    val trianglePath = Path().apply {
        // Moves to top center position
        moveTo(size / 2f + triangle.horizontalOffset, triangle.verticalOffset)
        // Add line to bottom right corner
        lineTo(size + triangle.horizontalOffset, size + triangle.verticalOffset)
        // Add line to bottom left corner
        lineTo(triangle.horizontalOffset, size +  triangle.verticalOffset)
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .scale(shapeSize)
    ) {
        drawPath(path = trianglePath, color = triangle.color)
    }
}

@Composable
private fun DrawSquareShape(square:Shape) {
    var updatedVisible by remember { mutableStateOf(square.visible) }

    val shapeSize by animateFloatAsState(
        targetValue = if (updatedVisible) 0F else 1F, label = "",
        animationSpec = tween(1000)
    )
    LaunchedEffect(square) {
        updatedVisible = true
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .scale(shapeSize)
    ) {
        drawRect(
            color = square.color,
            topLeft = Offset(square.horizontalOffset, square.verticalOffset),
            size = Size(450F, 450F)
        )
    }
}

@Composable
private fun DrawPentagonShape(pentagon:Shape) {
    var updatedVisible by remember { mutableStateOf(pentagon.visible) }

    val shapeSize by animateFloatAsState(
        targetValue = if (updatedVisible) 0F else 1F, label = "",
        animationSpec = tween(1000)
    )

    val size = 450F * shapeSize / 2
    LaunchedEffect(pentagon) {
        updatedVisible = true
    }
    val pentagonPath = Path().apply {
        // Moves to top center position
        moveTo(
            x = size + pentagon.horizontalOffset + (size * cos(0.0)).toFloat(),
            y = size + pentagon.verticalOffset + (size * sin(0.0)).toFloat()
        )
        for (i in 1..4) {
            val angle = 2.0 * Math.PI / 5
            lineTo(
                x = size + pentagon.horizontalOffset + (size * cos(angle * i)).toFloat(),
                y = size + pentagon.verticalOffset + (size * sin(angle * i)).toFloat()
            )
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .scale(shapeSize)
    ) {
        drawPath(path = pentagonPath, color = pentagon.color)
    }
}

@Composable
private fun DrawHexagonShape(hexagon: Shape) {
    var updatedVisible by remember { mutableStateOf(hexagon.visible) }

    val shapeSize by animateFloatAsState(
        targetValue = if (updatedVisible) 0F else 1F, label = "",
        animationSpec = tween(1000)
    )

    val size = 450F * shapeSize / 2
    LaunchedEffect(hexagon) {
        updatedVisible = true
    }
    val hexagonPath = Path().apply {
        // Moves to top center position
        moveTo(
            x = size + hexagon.horizontalOffset + (size * cos(0.0)).toFloat(),
            y = size + hexagon.verticalOffset + (size * sin(0.0)).toFloat()
        )
        for (i in 1..5) {
            val angle = 2.0 * Math.PI / 6
            lineTo(
                x = size + hexagon.horizontalOffset + (size * cos(angle * i)).toFloat(),
                y = size + hexagon.verticalOffset + (size * sin(angle * i)).toFloat()
            )
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .scale(shapeSize)
    ) {
        drawPath(path = hexagonPath, color = hexagon.color)
    }
}
@Preview(showBackground = true)
@Composable
fun GamePreview() {
    RememberTheBeatTheme {
        GameScreen(navController = rememberNavController())
    }
}