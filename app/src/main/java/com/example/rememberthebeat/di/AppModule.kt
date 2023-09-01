package com.example.rememberthebeat.di

import android.content.Context
import androidx.room.Room
import com.example.rememberthebeat.data.ScoreDao
import com.example.rememberthebeat.data.ScoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideScoreDao(database: ScoreDatabase): ScoreDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ScoreDatabase {
        return Room
            .databaseBuilder(
                appContext,
                ScoreDatabase::class.java,
                "score.db"
            )
            .build()
    }
}