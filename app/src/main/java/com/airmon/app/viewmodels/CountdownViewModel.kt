package com.airmon.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import java.time.LocalDateTime

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountdownViewModel : ViewModel() {
    private val now = LocalDateTime.now()
    private val _timer = MutableStateFlow((3600 * (23 - now.hour) + 60 * (59 - now.minute) + (59 - now.second)))
    val timer = _timer.asStateFlow()
    private var timerJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value--
            }
        }
    }
}
