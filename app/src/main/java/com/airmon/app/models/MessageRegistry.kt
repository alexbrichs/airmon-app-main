package com.airmon.app.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@RequiresApi(Build.VERSION_CODES.O)
object MessageRegistry {
    private val _messages: MutableLiveData<List<Message>> = MutableLiveData(listOf())
    val messages: LiveData<List<Message>> = _messages

    fun addMessage(message: Message) {
        val currentMessages = _messages.value?.toMutableList() ?: mutableListOf()
        currentMessages.add(message)
        _messages.value = currentMessages
    }

    fun addMessageWS(message: Message) {
        val currentMessages = _messages.value?.toMutableList() ?: mutableListOf()
        currentMessages.add(message)
        _messages.postValue(currentMessages)
    }

    fun clearMessages() {
        _messages.value = emptyList()
    }
}
