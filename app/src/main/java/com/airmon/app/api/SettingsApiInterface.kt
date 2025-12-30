package com.airmon.app.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface SettingsApiInterface {
    @POST("/api/edit-user/")
    @Headers("Accept: application/json")
    suspend fun editUser(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody?
    ): Response<Void>
}
