package com.example.rememberthebeat.game

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememberthebeat.data.ScoreDao
import com.example.rememberthebeat.data.models.HighScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class GameViewModel @Inject constructor(
    private val dao: ScoreDao
): ViewModel(){
    private var clickCount = 0
    private var clickTimes = mutableListOf<Long>()
    private var _scores = mutableListOf<Int>()
    val scores: List<Int>
        get() = _scores
    private var correctTiming = mutableListOf<Long>()
    private var gameText = mutableStateOf("tap along to the beat")
    private var gameVolume = 1F
    var state = mutableStateOf("game")
    private var totalScore = 0
    private var highScore = mutableIntStateOf(0)
    fun incrementClickCount() {
        clickCount++
    }

    private fun addClickTime(time: Long){
        if (clickCount in 21..40){
            clickTimes.add(time)
        }
    }
    private fun setCorrectTiming(time: Long){
        for (i in 1..20) {
            correctTiming.add(time + i * 500)
        }
    }
    private fun calculateScores(){
        for (i in 1 until clickTimes.size) {
            val timeDiff = clickTimes[i] - correctTiming[i]
            val score = (timeDiff / 10).coerceAtMost(50).coerceAtLeast(-50)
            _scores.add(score.toInt())
        }
    }

    private fun setTotalScore(){
        totalScore = scores.sumOf{ 50 - abs(it) }
    }

    fun updateGameState(){
        when (clickCount) {
            in 0 .. 3 -> {
                gameText.value = "tap along to the beat"
            }
            in 4..11 -> {
                gameText.value = "that's great!\n\nnow we'll see how well you can play on your own."
            }
            in 12..15 -> {
                gameText.value = "the backing track is going to fade out"
                gameVolume = 1F
            }
            16 -> {
                gameText.value = "the backing track is going to fade out\n\n5"
                gameVolume = 0.8F
            }
            17 -> {
                gameText.value = "the backing track is going to fade out\n\n4"
                gameVolume = 0.6F
            }
            18 -> {
                gameText.value = "the backing track is going to fade out\n\n3"
                gameVolume = 0.4F
            }
            19 -> {
                gameText.value = "the backing track is going to fade out\n\n2"
                gameVolume = 0.2F
            }
            20 -> {
                gameText.value = "the backing track is going to fade out\n\n1"
                gameVolume = 0F
                setCorrectTiming(System.currentTimeMillis())
            }
            in 21..34 -> {
                gameText.value = "keep playing!"
                addClickTime(System.currentTimeMillis())
            }
            35 -> {
                gameText.value = "nearly there! you can stop in\n\n5"
                addClickTime(System.currentTimeMillis())
            }
            36 -> {
                gameText.value = "nearly there! you can stop in\n\n4"
                addClickTime(System.currentTimeMillis())
            }
            37 -> {
                gameText.value = "nearly there! you can stop in\n\n3"
                addClickTime(System.currentTimeMillis())
            }
            38 -> {
                gameText.value = "nearly there! you can stop in\n\n2"
                addClickTime(System.currentTimeMillis())
            }
            39 -> {
                gameText.value = "nearly there! you can stop in\n\n1"
                addClickTime(System.currentTimeMillis())
            }
            40 -> {
                gameText.value = ""
                addClickTime(System.currentTimeMillis())
                calculateScores()
                setTotalScore()
                state.value = "summary"
                viewModelScope.launch {
                    dao.upsertHighScore(HighScore(totalScore))
                    highScore.intValue = dao.getHighestScore().score
                }
            }
        }
    }
    fun getGameText(): String {
        return gameText.value
    }

    fun getGameVolume(): Float {
        return gameVolume
    }

    fun getTotalScore(): Int {
        return totalScore
    }

    fun getHighScore():Int{
        return highScore.intValue
    }
}