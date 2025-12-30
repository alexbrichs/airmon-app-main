package com.airmon.app.api

import com.airmon.app.models.Airmon
import com.airmon.app.models.ApiAirmon
import com.airmon.app.models.DisplayAirmon
import com.airmon.app.models.SpawnedList
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AirmonsApiInterface {
    @GET("/api/airmons/")
    suspend fun getAirmons(@HeaderMap headers: Map<String, String>): Response<List<Airmon>>

    @GET("/api/airmons/{name}/")
    suspend fun getAirmon(@HeaderMap headers: Map<String, String>, @Path("name") name: String): Response<ApiAirmon>

    @GET("/api/spawned_airmons/")
    suspend fun getNearbyAirmons(
        @HeaderMap headers: Map<String, String>,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Response<SpawnedList>

    @FormUrlEncoded
    @POST("/api/captures/")
    suspend fun captureAirmon(@HeaderMap headers: Map<String, String>,
                              @Field("spawned_airmon_id") spawnedId: Int): Response<DisplayAirmon>

    @GET("/api/player/captures/")
    suspend fun getCaptures(@HeaderMap headers: Map<String, String>): Response<List<DisplayAirmon>>

    @DELETE("/api/captures/{id}/")
    suspend fun deleteCapturedAirmon(@HeaderMap headers: Map<String, String>, @Path("id") id: Int): Response<Void>
}
