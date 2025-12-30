package com.airmon.app.api

import android.app.Application

import com.airmon.app.helpers.getGetApiHeaders
import com.airmon.app.helpers.getPostApiHeaders
import com.airmon.app.models.DisplayAirmon
import com.airmon.app.models.Item
import com.airmon.app.models.ItemInventory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.runBlocking

object ItemApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ItemApiInterface::class.java)

    fun getItems(application: Application): HashMap<String, Item> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Item>>
        runBlocking {
            response = apiService.getItems(headers)
        }
        val itemMap: HashMap<String, Item> = HashMap()
        if (response.isSuccessful) {
            val items: List<Item>? = response.body()
            items?.forEach { item ->
                itemMap[item.name] = item
            }
        }
        return itemMap
    }

    fun getPlayerItems(application: Application): HashMap<String, ItemInventory> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<ItemInventory>>
        runBlocking {
            response = apiService.getPlayerItems(headers)
        }
        val itemMap: HashMap<String, ItemInventory> = HashMap()
        if (response.isSuccessful) {
            val itemsInventory: List<ItemInventory>? = response.body()
            itemsInventory?.forEach { itemInventory ->
                itemMap[itemInventory.item_name] = itemInventory
            }
        }
        return itemMap
    }

    fun airboxItem(application: Application, item: String): DisplayAirmon? {
        val headers = getPostApiHeaders(application)
        var response: Response<DisplayAirmon>
        runBlocking {
            response = apiService.airboxItem(headers, item)
        }
        if (response.isSuccessful) {
            return response.body()!!
        }
        return null
    }
}
