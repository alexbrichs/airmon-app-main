package com.airmon.app.api

import com.airmon.app.models.Message
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface ChatApiInterface {
    @GET("/api/chat/{id}")
    suspend fun getChat(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<List<Message>>

    @GET("/api/chat/{id}?last=true")
    suspend fun getLastMessage(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<Message>
}
