package com.example.rememberthebeat.data.models

import androidx.compose.ui.graphics.Color

data class Shape(
    val type: String,
    var visible: Boolean = false,
    val horizontalOffset: Float = 0F,
    val verticalOffset: Float = 0F,
    val color: Color
)
