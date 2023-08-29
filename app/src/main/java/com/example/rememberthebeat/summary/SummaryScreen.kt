package com.example.rememberthebeat.summary

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.ui.theme.RememberTheBeatTheme
import kotlin.math.abs

@Composable
fun SummaryScreen(
    scores: String,
    navController: NavController
){
    val scoreList = scores.split(",").map{ it.toInt() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ){

        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .align(Alignment.Center)
                .padding(36.dp)
        ) {
            Row {
                Text(
                    text = scoreList.sumOf{ 50 - abs(it) }.toString() + "\nYour Score",
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
                    text = scoreList.sumOf{ 50 - abs(it) }.toString() + "\nHigh Score",
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
                    for (i in scoreList){
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
                    for (i in scoreList){
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

            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Play Again")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryPreview() {
    RememberTheBeatTheme {
        SummaryScreen(
            scores ="-50,-45,-40,-35,-30,-25,-20,-15,-10,-5,0,5,10,15,20,25,30,35,40,45",
            navController = rememberNavController()
        )
    }
}