package com.airmon.app.viewmodels

import androidx.lifecycle.ViewModel
import com.airmon.app.models.ItemRegistry

/**
 * @param itemName
 */
class ItemInfoShopViewModel(itemName: String) : ViewModel() {
    val name = itemName
    var price: Int = 0
    lateinit var description: String
    lateinit var image: String
    lateinit var duration: String

    init {
        val item = ItemRegistry.getItem(itemName)
        item?.let {
            description = item.description
            image = item.image
            duration = item.duration
            price = item.price
        }
    }
}
