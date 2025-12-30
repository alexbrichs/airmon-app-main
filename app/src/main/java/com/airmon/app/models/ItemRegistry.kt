package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ItemApiInterfaceImplementation

object ItemRegistry {
    private var itemMap: HashMap<String, Item> = HashMap()

    fun getItem(name: String): Item? = itemMap[name]

    fun getAllItemNames(): List<String> = itemMap.keys.toList()

    fun getItems(application: Application) {
        itemMap = ItemApiInterfaceImplementation.getItems(application)
    }

    fun getItemImage(name: String): String = itemMap[name]?.image ?: ""
    fun getDuration(item: String): String = itemMap[item]?.duration ?: ""
}
