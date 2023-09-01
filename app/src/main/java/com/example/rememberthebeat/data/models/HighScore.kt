package com.example.rememberthebeat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HighScore(
    @PrimaryKey
    val score: Int
)
