package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.MapApiInterfaceImplementation

object StationRegistry {
    private var stationMap: HashMap<String, Station> = HashMap()

    fun getStation(name: String): Station? = stationMap[name]

    fun getAllStationNames(): List<String> = stationMap.keys.toList()

    fun getStations(application: Application) {
        stationMap = MapApiInterfaceImplementation.getStations(application)
    }

    fun getStationNamesOrderedByICQA(query: String): List<String> = if (query.isEmpty()) {
        stationMap.keys.sortedBy { stationMap[it]?.measure?.icqa }
    } else {
        stationMap.keys
            .filter { it.contains(query, ignoreCase = true) }
            .sortedBy { stationMap[it]?.measure?.icqa }
    }
}
