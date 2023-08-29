package com.example.rememberthebeat.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel(){
    private val _clickCount = MutableStateFlow(0)
    val clickCount: StateFlow<Int> = _clickCount
    fun incrementClickCount() {
        viewModelScope.launch {
            _clickCount.value++
        }
    }
    var clickTimes = mutableListOf<Long>()
    var scores = mutableListOf<Int>()
    var correctTimes = mutableListOf<Long>()
    var sendThis = "50,-50"
    var trigger = true
    fun addClickTime(time: Long){
        clickTimes.add(time)
    }
    fun setStart(time: Long){
        for (i in 1..20) {
            correctTimes.add(time + i * 500)
        }
    }
    fun calculateScores(){
        for (i in 1 until clickTimes.size){
            val timeDiff = clickTimes[i] - correctTimes[i]
            val score = (timeDiff/10).coerceAtMost(50).coerceAtLeast(-50)
            scores.add(score.toInt())
        }
        sendThis = scores.joinToString(",")
    }
    fun flip(){
        trigger = false
    }
}