package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.helpers.retrieveFromDB
import com.airmon.app.models.Item
import com.airmon.app.models.ItemRegistry
import com.airmon.app.models.PlayerRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

/**
 * @param application
 */
class ShopViewModel(application: Application) : ViewModel() {
    private val app = application
    private var username: String
    private val _coins = MutableStateFlow(0)
    val coins: StateFlow<Int> = _coins

    init {
        runBlocking {
            username = retrieveFromDB(app, "username") ?: ""
        }
        _coins.value = PlayerRegistry.getPlayer(app, username)?.coins ?: 0
    }

    fun getListItems(): List<String> = ItemRegistry.getAllItemNames()
    fun getItem(name: String): Item? = ItemRegistry.getItem(name)

    fun modifyCoins(newCoins: Int) {
        _coins.value += newCoins
    }

    fun buyItem(item: Item, qtt: Int) {
        if (_coins.value >= item.price * qtt) {
            PlayerRegistry.buyItem(app, item.name, qtt)
            modifyCoins(-item.price * qtt)
        }
    }
}
