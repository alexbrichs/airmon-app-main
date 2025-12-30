package com.airmon.app.api

import android.app.Application

import com.airmon.app.helpers.getGetApiHeaders
import com.airmon.app.models.Message
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.runBlocking

object ChatApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ChatApiInterface::class.java)

    fun getChat(application: Application, id: Int): List<Message> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Message>>
        runBlocking {
            response = apiService.getChat(headers, id)
        }

        val messages: List<Message> = response.body() ?: emptyList()

        return messages
    }

    fun getLastMessage(application: Application, id: Int): Message {
        val headers = getGetApiHeaders(application)
        var response: Response<Message>
        runBlocking {
            response = apiService.getLastMessage(headers, id)
        }

        val last: Message = response.body() ?: Message("", "", "", "", true)
        return last
    }
}
