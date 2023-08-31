package com.example.rememberthebeat.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.R
import com.example.rememberthebeat.ui.theme.RememberTheBeatTheme

@Composable
fun StartScreen(
    navController: NavController,
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan)
    ){

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = "Got Rhythm?",
                fontSize = 42.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
            Text(
                text = "Can you keep the rhythm when the beat drops out?",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            )
            Text(
                text = "turn up your volume and hit play to begin",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
            )
            Button(
                onClick = { navController.navigate("game_screen") },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                modifier = Modifier
                    .size(144.dp)
                    .align(CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play_icon),
                    contentDescription = "play icon",
                    modifier = Modifier
                        .size(75.dp)
                        .padding(start=10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    RememberTheBeatTheme {
        StartScreen(navController = rememberNavController())
    }
}