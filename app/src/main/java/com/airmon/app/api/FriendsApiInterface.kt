/**
 * @property user
 */

package com.airmon.app.api

import com.airmon.app.models.Estadistiques
import com.airmon.app.models.FriendUser
import com.airmon.app.models.Logro
import com.airmon.app.models.Player
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendsApiInterface {
    @GET("/api/friendship/")
    suspend fun getFriends(
        @HeaderMap headers: Map<String, String>,
    ): Response<List<FriendUser>>

    @GET("/api/find-user/{text}/")
    suspend fun find_user(
        @HeaderMap headers: Map<String, String>,
        @Path("text") text: String
    ): Response<List<Username>>

    @FormUrlEncoded
    @POST("/api/friendship/")
    suspend fun addFriend(
        @HeaderMap headers: Map<String, String>,
        @Field("user") user: String
    ): Response<ChatId>

    @DELETE("/api/friendship/")
    suspend fun unFriend(
        @HeaderMap headers: Map<String, String>,
        @Query("user") user: String
    )

    @GET("/api/posts/{username}/")
    suspend fun getFotos(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String
    ): Response<List<Foto>>

    @GET("/api/player/{username}/")
    suspend fun getAvatar(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String
    ): Response<Player>

    @GET("/api/{username}/statistics/")
    suspend fun getEstadistiques(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String
    ): Response<Estadistiques>

    @GET("/api/trophies/{username}/")
    suspend fun getLogros(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String
    ): Response<List<Logro>>
}

/**
 * @property username
 */
data class Username(
    val username: String
)

/**
 * @property chat_id
 */
data class ChatId(
    val chat_id: Int
)

/**
 * @property image
 */

/**
 * @property date
 * @property user
 * @property image
 */
data class Foto(
    val user: Int,
    val image: String,
    val date: String
)
