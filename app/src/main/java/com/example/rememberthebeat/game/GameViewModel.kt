package com.example.rememberthebeat.game

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
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
            scores.add((clickTimes[i] - correctTimes[i]).toInt())
        }
    }
}