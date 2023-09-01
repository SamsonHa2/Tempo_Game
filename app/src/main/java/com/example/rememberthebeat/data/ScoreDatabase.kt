package com.example.rememberthebeat.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rememberthebeat.data.models.HighScore

@Database(
    entities = [HighScore::class],
    version = 1
)
abstract class ScoreDatabase: RoomDatabase() {
    abstract val dao: ScoreDao
}