package com.example.rememberthebeat.summary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememberthebeat.ui.theme.RememberTheBeatTheme

@Composable
fun SummaryScreen(navController: NavController){

}

@Preview(showBackground = true)
@Composable
fun SummaryPreview() {
    RememberTheBeatTheme {
        SummaryScreen(navController = rememberNavController())
    }
}