package com.airmon.app.api

import com.airmon.app.models.ApiSpin
import com.airmon.app.models.ItemActive
import com.airmon.app.models.Player
import com.airmon.app.models.Token
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApiInterface {
    @FormUrlEncoded
    @POST("/api/login/")
    suspend fun login(@Field("username") username: String,
                      @Field("password") password: String): Response<Token>

    @FormUrlEncoded
    @POST("/api/register/")
    suspend fun register(@Field("username") username: String,
                         @Field("password") password: String): Response<Token>
    @POST("/api/images/")
    @Headers("Accept: application/json")
    suspend fun uploadImage(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Response<Void>

    @GET("/api/player/{name}/trophies/")
    suspend fun getLogros(
        @HeaderMap headers: Map<String, String>,
        @Path("name") name: String
    ): Response<LogroProgre>

    @GET("/api/player/{username}/")
    suspend fun getPlayer(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String
    ): Response<Player>

    @FormUrlEncoded
    @POST("/api/player/items/")
    suspend fun buyItem(
        @HeaderMap headers: Map<String, String>,
        @Field("item_name") item: String,
        @Field("quantity") qtt: Int,
        @Field("free") free: Boolean,
    ): Response<Void>

    @GET("/api/player/active-items/")
    suspend fun getActiveItems(
        @HeaderMap headers: Map<String, String>
    ): Response<List<ItemActive>>

    @FormUrlEncoded
    @POST("/api/player/active-items/")
    suspend fun activeItem(
        @HeaderMap headers: Map<String, String>,
        @Field("item_name") item: String,
    ): Response<Void>

    @FormUrlEncoded
    @PUT("/api/player/exp/")
    suspend fun addXP(
        @HeaderMap headers: Map<String, String>,
        @Field("exp") xp: String,
    ): Response<Void>

    @FormUrlEncoded
    @POST("/api/player/coins/")
    suspend fun addCoins(
        @HeaderMap headers: Map<String, String>,
        @Field("coins") coins: String,
    ): Response<Void>

    @GET("/api/player/roulette/")
    suspend fun getLastSpin(@HeaderMap headers: Map<String, String>): Response<ApiSpin>

    @POST("/api/player/roulette/")
    suspend fun postSpin(@HeaderMap headers: Map<String, String>): Response<ApiSpin>
}

/**
 * @property id
 * @property name
 * @property type
 * @property description
 * @property requirement
 * @property xp
 * @property progress
 */
data class LogroProgre(
    val id: Int,
    val name: String,
    val type: String,
    val description: String,
    val requirement: Int,
    val xp: Int,
    val progress: Int
)
