package com.airmon.app.api

import android.app.Application
import com.airmon.app.helpers.getGetApiHeaders
import com.airmon.app.helpers.getPostApiHeaders
import com.airmon.app.models.Estadistiques
import com.airmon.app.models.FriendUser
import com.airmon.app.models.Logro
import com.airmon.app.models.Player
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking

object FriendsApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(FriendsApiInterface::class.java)

    fun getFriends(application: Application): HashMap<String, FriendUser> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<FriendUser>>
        runBlocking {
            response = apiService.getFriends(headers)
        }
        val friendMap: HashMap<String, FriendUser> = HashMap()
        if (response.isSuccessful) {
            val friends: List<FriendUser>? = response.body()
            friends?.forEach { friend ->
                friendMap[friend.username] = friend
            }
        }
        return friendMap
    }

    fun find_user(application: Application, text: String): MutableList<String> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Username>>
        runBlocking {
            response = apiService.find_user(headers, text)
        }
        val userList: MutableList<String> = mutableListOf()
        if (response.isSuccessful) {
            val users: List<Username>? = response.body()
            users?.let {
                users.forEach {
                    userList.add(it.username)
                }
            }
        }
        return userList
    }

    fun addFriend(application: Application, user: String): Int? {
        val headers = getPostApiHeaders(application)
        var response: Response<ChatId>
        runBlocking {
            response = apiService.addFriend(headers, user)
        }
        if (response.isSuccessful) {
            val chat_id = response.body()!!
            return chat_id?.chat_id
        }
        return null
    }

    fun unFriend(application: Application, user: String) {
        val headers = getPostApiHeaders(application)
        runBlocking {
            apiService.unFriend(headers, user)
        }
    }

    fun getFotos(application: Application, user: String): List<String> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Foto>>
        runBlocking {
            response = apiService.getFotos(headers, user)
        }
        var fotosList: MutableList<String> = mutableListOf()
        if (response.isSuccessful) {
            val fotos: List<Foto>? = response.body()
            fotos?.forEach { it ->
                fotosList.add(it.image)
            }
        }
        return fotosList
    }

    fun getAvatar(application: Application, user: String): String? {
        val headers = getGetApiHeaders(application)
        val response: Response<Player>
        runBlocking {
            response = apiService.getAvatar(headers, user)
        }
        if (response.isSuccessful) {
            return response.body()!!.avatar
        }
        return null
    }

    fun getEstadistiques(application: Application, user: String): Estadistiques? {
        val headers = getGetApiHeaders(application)
        val response: Response<Estadistiques>
        runBlocking {
            response = apiService.getEstadistiques(headers, user)
        }
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    fun getLogros(application: Application, user: String): List<Logro>? {
        val headers = getGetApiHeaders(application)
        val response: Response<List<Logro>>
        runBlocking {
            response = apiService.getLogros(headers, user)
        }
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    fun getExp(application: Application, user: String): Int? {
        val headers = getGetApiHeaders(application)
        val response: Response<Player>
        runBlocking {
            response = apiService.getAvatar(headers, user)
        }
        if (response.isSuccessful) {
            return response.body()!!.xp_points
        }
        return null
    }
}
