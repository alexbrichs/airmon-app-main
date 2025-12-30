package com.airmon.app.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.airmon.app.models.FriendUserRegistry
import com.airmon.app.models.MessageRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @param application
 */
@RequiresApi(Build.VERSION_CODES.O)
class MainChatsViewModel(private val application: Application) : ViewModel() {
    private val observableListFriends: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val listFriends: StateFlow<List<String>> = observableListFriends
    private val friendReadStatus: MutableMap<String, Boolean> = mutableMapOf()

    init {
        MessageRegistry.clearMessages()
        observableListFriends.value = FriendUserRegistry.getAllFriendNames()
        for (name in observableListFriends.value) {
            val lastMessage = FriendUserRegistry.getLastMessage(application, name)
            if (lastMessage.sender == name) {
                friendReadStatus[name] = lastMessage.read
            }
        }
    }

    fun isReadStatus(friendName: String): Boolean = friendReadStatus[friendName] ?: true

    fun getAvatar(username: String): String? =
        FriendUserRegistry.getAvatar(application, username)
}
