package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ChatApiInterfaceImplementation
import com.airmon.app.api.FriendsApiInterfaceImplementation
import com.airmon.app.utils.formatDate
import java.util.Date

object FriendUserRegistry {
    private var friendMap: HashMap<String, FriendUser> = HashMap()

    fun getFriend(name: String): FriendUser? = friendMap[name]

    fun getAllFriendNames(): List<String> = friendMap.keys.toList()

    fun getFriends(application: Application) {
        friendMap = FriendsApiInterfaceImplementation.getFriends(application)
    }

    fun getChat(application: Application, name: String): List<Message> = ChatApiInterfaceImplementation.getChat(
        application, friendMap[name]!!.chat_id
    )

    fun getLastMessage(application: Application, name: String): Message = ChatApiInterfaceImplementation.getLastMessage(
        application, friendMap[name]!!.chat_id
    )
    
    fun getUsers(application: Application, text: String): MutableList<String> =
        FriendsApiInterfaceImplementation.find_user(application, text)

    fun isFriend(username: String): Boolean =
        friendMap.containsKey(username)

    fun addFriend(application: Application, user: String) {
        val chat_id = FriendsApiInterfaceImplementation.addFriend(application, user)
        chat_id?.let {
            friendMap[user] = FriendUser(user, chat_id, formatDate(Date()))
        }
    }

    fun unFriend(application: Application, user: String) {
        friendMap.remove(user)
        FriendsApiInterfaceImplementation.unFriend(application, user)
    }

    fun getFotos(application: Application, user: String): List<String> =
        FriendsApiInterfaceImplementation.getFotos(application, user)

    fun getAvatar(application: Application, user: String): String? =
        FriendsApiInterfaceImplementation.getAvatar(application, user)

    fun getEstadistiques(application: Application, user: String): Estadistiques? =
        FriendsApiInterfaceImplementation.getEstadistiques(application, user)

    fun getLogros(application: Application, user: String): List<Logro>? =
        FriendsApiInterfaceImplementation.getLogros(application, user)

    fun getExp(application: Application, user: String): Int? =
        FriendsApiInterfaceImplementation.getExp(application, user)
}
