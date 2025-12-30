package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.AirmonsApiInterfaceImplementation
import com.airmon.app.api.ItemApiInterfaceImplementation

object AirmonCollectionRegistry {
    private var collection: HashMap<Int, DisplayAirmon> = HashMap()

    fun getAllCaptures(): List<Pair<Int, String>> = collection.entries.map { entry ->
        entry.key to entry.value.airmon
    }

    fun getCapture(id: Int): DisplayAirmon? = collection[id]

    fun getCollection(application: Application) {
        collection = AirmonsApiInterfaceImplementation.getCapturedAirmons(application)
    }

    fun captureAirmons(application: Application, spwanedId: Int) {
        AirmonsApiInterfaceImplementation.captureAirmon(application, spwanedId)
    }

    fun airboxItem(application: Application): Int? {
        val airmon = ItemApiInterfaceImplementation.airboxItem(application, "airbox")
        airmon?.let {
            collection[airmon.id] = airmon
            return airmon.id
        }
        return null
    }
}
