package com.airmon.app

import android.os.Build
import androidx.annotation.RequiresApi
import com.airmon.app.viewmodels.ChatViewModel
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * @param viewModel
 */
class ChatWebSocketListener(private val viewModel: ChatViewModel) : WebSocketListener() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessage(webSocket: WebSocket, text: String) {
        viewModel.onMessageReceived(text)
    }
}
