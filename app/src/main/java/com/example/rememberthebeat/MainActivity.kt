package com.example.rememberthebeat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.game.GameScreen
import com.example.rememberthebeat.start.StartScreen
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
                        BackHandler(true) {
                            // Or do nothing
                            Log.i("LOG_TAG", "Clicked back")
                        }
                        GameScreen(navController = navController)
                    }
                }
            }
        }
    }
}