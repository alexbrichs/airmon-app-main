package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.MapApiInterfaceImplementation

object EventRegistry {
    private var eventMap: HashMap<String, Event> = HashMap()

    fun getEvent(name: String): Event? = eventMap[name]

    fun getAllEventNames(): List<String> = eventMap.keys.toList()

    fun getEvents(application: Application) {
        eventMap = MapApiInterfaceImplementation.getEvents(application)
    }
}
