package com.airmon.app.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.airmon.app.models.ItemActiveRegistry
import com.airmon.app.models.ItemInventoryRegistry
import com.airmon.app.models.ItemRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @param itemName
 * @param application
 */
class ItemInfoInventoryViewModel(application: Application, itemName: String) : ViewModel() {
    private val app = application
    val name = itemName
    var price: Int = 0
    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity
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
        ItemInventoryRegistry.getItemInventory(name)?.let {
            _quantity.value = it.quantity
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addActiveItem() {
        if (_quantity.value == 0) {
            return
        } else {
            ItemActiveRegistry.addActiveItem(app, name)
            _quantity.value -= 1
        }
    }

    fun removeItem() {
        ItemInventoryRegistry.removeItem(name)
    }

    fun updateItemQuantity() {
        ItemInventoryRegistry.updateQuantity(name)
        _quantity.value--
    }
}
