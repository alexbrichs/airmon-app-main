package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ProfileApiInterfaceImplementation

object PlayerRegistry {
    fun getPlayer(application: Application, username: String): Player? =
        ProfileApiInterfaceImplementation.getPlayer(
            application, username
        )

    fun buyItem(
        application: Application,
        item: String,
        qtt: Int
    ) {
        ProfileApiInterfaceImplementation.buyItem(application, item, qtt, false)
    }

    fun addXP(application: Application, xp: String) {
        ProfileApiInterfaceImplementation.addXP(application, xp)
    }

    fun addCoins(application: Application, coins: String) {
        ProfileApiInterfaceImplementation.addCoins(application, coins)
    }
}
