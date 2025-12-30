package com.airmon.app.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.airmon.app.ChatWebSocketListener
import com.airmon.app.models.FriendUserRegistry
import com.airmon.app.models.Message
import com.airmon.app.models.MessageRegistry
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

import kotlinx.coroutines.launch

/**
 * @param friendName
 * @param application
 * @param token
 * @param username
 */
@RequiresApi(Build.VERSION_CODES.O)
class ChatViewModel(
    private val application: Application,
    friendName: String,
    token: String?,
    username: String?
) : ViewModel() {
    val name = friendName
    private val _id = FriendUserRegistry.getFriend(name)?.chat_id
    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messages
    private var _currentUser = username
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var _token = token
    private var gson = Gson()
    private lateinit var listener: ChatWebSocketListener

    init {
        viewModelScope.launch {
            val chat = FriendUserRegistry.getChat(application, friendName)
            chat.let {
                for (message in chat) {
                    if (!MessageRegistry.messages.value?.contains(message)!!) {
                        MessageRegistry.addMessage(message)
                    }
                }
            }

            MessageRegistry.messages.observeForever { messages ->
                _messages.value = messages
            }
        }
    }

    fun getCurrentUser(): String? = _currentUser

    fun startChat() {
        listener = ChatWebSocketListener(this)
        val request =
            Request.Builder().url("ws://51.21.149.211/api/ws/chat/$_id/?token=$_token").build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun closeChat() {
        webSocket!!.close(1000, "Bye!")
        MessageRegistry.clearMessages()
        MessageRegistry.messages.removeObserver { messages ->
            _messages.value = messages
        }
    }

    fun addMessage(content: String) {
        webSocket?.send("{\"content\": \"$content\"}")
    }

    fun onMessageReceived(content: String) {
        val message = gson.fromJson(content, Message::class.java)
        if (!MessageRegistry.messages.value?.contains(message)!!) {
            MessageRegistry.addMessageWS(message)
        }
        // _messages.postValue(MessageRegistry.messages.value)
    }

    fun getAvatar(username: String): String? =
        FriendUserRegistry.getAvatar(application, username)
}
