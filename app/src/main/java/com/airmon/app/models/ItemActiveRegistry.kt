package com.airmon.app.models

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.airmon.app.api.ProfileApiInterfaceImplementation
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object ItemActiveRegistry {
    private var itemMap: HashMap<String, ItemActive> = HashMap()

    fun getItemActive(name: String): ItemActive? = itemMap[name]

    fun getAllItemsActive(): List<String> = itemMap.keys.toList()

    fun getItemsActive(application: Application) {
        itemMap = ProfileApiInterfaceImplementation.getActiveItems(application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToLocalTime(time: String): LocalTime = LocalTime.parse(time)

    @RequiresApi(Build.VERSION_CODES.O)
    fun addActiveItem(application: Application, item: String) {
        ProfileApiInterfaceImplementation.activeItem(application, item)

        val durationString = ItemRegistry.getDuration(item)
        if (durationString == "00:00:00") {
            return
        }
        val duration = convertToLocalTime(durationString)

        val itemActive = getItemActive(item)
        val zoneId = ZoneId.of("Europe/Madrid")
        itemActive?.let {
            val expirationDate = ZonedDateTime.parse(itemActive.expiration)
            val durationInSeconds = duration.toSecondOfDay().toLong()
            val newExpirationDate = expirationDate.plusSeconds(durationInSeconds)
            itemMap[item] = ItemActive(item, newExpirationDate.toString())
        } ?: run {
            val now = ZonedDateTime.now(zoneId)
            val newExpirationDate = now.plusSeconds(duration.toSecondOfDay().toLong())
            itemMap[item] = ItemActive(item, newExpirationDate.toString())
        }
    }

    fun removeActiveItem(itemName: String) {
        itemMap.remove(itemName)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isActive(item: String): Boolean {
        val itemActive = getItemActive(item) ?: return false
        val now = ZonedDateTime.now()
        val expirationDate = ZonedDateTime.parse(itemActive.expiration)
        return now.isBefore(expirationDate)
    }
}
