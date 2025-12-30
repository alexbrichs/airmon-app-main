package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.models.FriendUserRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @param application
 */
class FriendsViewModel(application: Application) : ViewModel() {
    val app = application
    private val observableListFriends: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val listFriends: StateFlow<List<String>> = observableListFriends
    private val observableListUsers: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val listUsers: StateFlow<List<String>> = observableListUsers

    init {
        // FriendUserRegistry.getFriends(application = Application())
        observableListFriends.value = FriendUserRegistry.getAllFriendNames()
    }

    fun busca_usuaris(text: String): MutableList<String> =
        FriendUserRegistry.getUsers(app, text)

    fun isFriends(username: String): Boolean =
        FriendUserRegistry.isFriend(username)

    fun addFriend(username: String) {
        FriendUserRegistry.addFriend(app, username)
    }

    fun unFriend(username: String) {
        FriendUserRegistry.unFriend(app, username)
    }

    fun getFotos(username: String): List<String> =
        FriendUserRegistry.getFotos(app, username)

    fun getAvatar(username: String): String? =
        FriendUserRegistry.getAvatar(app, username)
}
