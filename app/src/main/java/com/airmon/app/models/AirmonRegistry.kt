package com.airmon.app.models

import android.app.Application
import android.graphics.Bitmap
import com.airmon.app.api.AirmonsApiInterfaceImplementation

object AirmonRegistry {
    private var airmonMap: HashMap<String, Airmon> = HashMap()
    private var imagesMap: HashMap<String, Bitmap?> = HashMap()
    private var nearbyAirmonsMap: HashMap<Int, SpawnedAirmon?> = HashMap()

    fun getAirmon(name: String): Airmon? = airmonMap[name]

    fun getAirmon(application: Application, name: String): Airmon? = AirmonsApiInterfaceImplementation.getAirmon(
        application, name
    )

    fun getAllAirmonNames(): List<String> = airmonMap.keys.toList()

    fun getAirmons(application: Application) {
        airmonMap = AirmonsApiInterfaceImplementation.getAirmons(application)
    }

    fun getNearbyAirmons(
        application: Application,
        latitude: Double,
        longitude: Double
    ): HashMap<Int, SpawnedAirmon> = AirmonsApiInterfaceImplementation.getNearbyAirmons(application, latitude,
        longitude)

    fun addBitmap(name: String, bitmap: Bitmap?) {
        imagesMap[name] = bitmap
    }

    fun getBitmap(name: String): Bitmap? = imagesMap[name]
}

/**
 * @property airmons
 */
data class SpawnedList(
    val airmons: List<SpawnedAirmon>
)

/**
 * @property name
 * @property spawned_airmon_id
 * @property location
 */
data class SpawnedAirmon(
    val name: String,
    val spawned_airmon_id: Int,
    val location: Location
)

/**
 * @property latitude
 * @property longitude
 */
data class Location(
    val latitude: Double,
    val longitude: Double
)
