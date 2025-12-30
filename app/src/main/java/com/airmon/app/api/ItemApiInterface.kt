package com.airmon.app.api

import com.airmon.app.models.DisplayAirmon
import com.airmon.app.models.Item
import com.airmon.app.models.ItemInventory
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ItemApiInterface {
    @GET("/api/items/")
    suspend fun getItems(
        @HeaderMap headers: Map<String, String>,
    ): Response<List<Item>>

    @GET("/api/player/items/")
    suspend fun getPlayerItems(
        @HeaderMap headers: Map<String, String>,
    ): Response<List<ItemInventory>>

    @FormUrlEncoded
    @POST("/api/player/active-items/")
    suspend fun airboxItem(
        @HeaderMap headers: Map<String, String>,
        @Field("item_name") item: String,
    ): Response<DisplayAirmon>
}
