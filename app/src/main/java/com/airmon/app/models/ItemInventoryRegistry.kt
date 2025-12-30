package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ItemApiInterfaceImplementation

object ItemInventoryRegistry {
    private var itemMap: HashMap<String, ItemInventory> = HashMap()

    fun getItemInventory(name: String): ItemInventory? = itemMap[name]

    fun getAllItemInventory(): List<String> = itemMap.keys.toList()

    fun getItemsInventory(application: Application) {
        itemMap = ItemApiInterfaceImplementation.getPlayerItems(application)
    }

    fun getItemImage(name: String): String = ItemRegistry.getItemImage(name)
    fun updateQuantity(item: String) {
        val itemInventory = itemMap[item]
        itemInventory?.let {
            itemMap[itemInventory.item_name] =
                    ItemInventory(itemInventory.item_name, itemInventory.quantity - 1)
        }
        if (itemInventory?.quantity == 0) {
            itemMap.remove(item)
        }
    }

    fun removeItem(name: String) {
        itemMap.remove(name)
    }
}
