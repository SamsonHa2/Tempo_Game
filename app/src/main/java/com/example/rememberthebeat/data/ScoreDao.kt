package com.example.rememberthebeat.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.rememberthebeat.data.models.HighScore

@Dao
interface ScoreDao {
    @Upsert
    suspend fun upsertHighScore(highScore: HighScore)

    @Query("SELECT * FROM highScore ORDER BY score ASC LIMIT 1")
    suspend fun getLowestScore(): HighScore

    @Query("SELECT * FROM highScore ORDER BY score DESC LIMIT 1")
    suspend fun getHighestScore(): HighScore

    @Delete
    suspend fun deleteHighScore(highScore: HighScore)
}