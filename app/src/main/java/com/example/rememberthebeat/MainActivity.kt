package com.example.rememberthebeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.game.GameScreen
import com.example.rememberthebeat.start.StartScreen
import com.example.rememberthebeat.summary.SummaryScreen
import com.example.rememberthebeat.ui.theme.RememberTheBeatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RememberTheBeatTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start_screen") {
                    composable("start_screen") {
                        StartScreen(navController = navController)
                    }
                    composable("game_screen"){
                        GameScreen(navController = navController)
                    }
                    composable("summary_screen") {
                        SummaryScreen(navController = navController)
                    }
                }
            }
        }
    }
}