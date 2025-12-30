package com.airmon.app.viewmodels

import android.app.Application
import com.airmon.app.api.LogroProgre
import com.airmon.app.api.ProfileApiInterfaceImplementation

/**
 * @param application
 * @param nameTrophy
 */
class TrophyInfoViewModel(application: Application, nameTrophy: String) {
    val name = nameTrophy
    val app = application

    fun getProximLogro(): LogroProgre? =
        ProfileApiInterfaceImplementation.getProximLogro(app, name)
}
