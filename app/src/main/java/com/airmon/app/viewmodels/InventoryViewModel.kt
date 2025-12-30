package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.models.ItemActiveRegistry
import com.airmon.app.models.ItemInventoryRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @param application
 */
class InventoryViewModel(application: Application) : ViewModel() {
    val app = application
    private val _listItems = ItemInventoryRegistry.getAllItemInventory()
    private val _listActiveItems: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val activeItems: StateFlow<List<String>> = _listActiveItems

    init {
        ItemInventoryRegistry.getItemsInventory(app)
        _listActiveItems.value = ItemActiveRegistry.getAllItemsActive()
    }

    fun getInventoryItems(): List<String> = _listItems

    fun getItemInventory(name: String) = ItemInventoryRegistry.getItemInventory(name)

    fun getItemActive(name: String) = ItemActiveRegistry.getItemActive(name)

    fun removeActiveItem(name: String) {
        ItemActiveRegistry.removeActiveItem(name)
        _listActiveItems.value = ItemActiveRegistry.getAllItemsActive()
    }

    fun getItemImage(name: String) = ItemInventoryRegistry.getItemImage(name)
}
