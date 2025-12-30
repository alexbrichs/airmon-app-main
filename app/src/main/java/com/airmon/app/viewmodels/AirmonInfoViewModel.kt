package com.airmon.app.viewmodels

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.airmon.app.api.AirmonsApiInterfaceImplementation
import com.airmon.app.models.AirmonCollectionRegistry
import com.airmon.app.models.AirmonRarity
import com.airmon.app.models.AirmonRegistry
import com.airmon.app.models.AirmonType
import com.airmon.app.models.ItemActiveRegistry
import com.airmon.app.models.PlayerRegistry
import com.airmon.app.utils.formatDateForDisplay

/**
 * @param Captureid
 * @property application
 */
class AirmonInfoViewModel(val application: Application, Captureid: Int) : ViewModel() {
    var id = Captureid
    var date = ""
    var name = ""
    var description = ""
    var rarity = AirmonRarity.COMMON
    var type = AirmonType.H2S
    var image = ""
    var releaseXP = 0
    var c: String? = null

    init {
        val capture = AirmonCollectionRegistry.getCapture(id)
        c = capture?.airmon
        capture?.let {
            date = formatDateForDisplay(capture.date)
            name = capture.airmon
            val airmon = AirmonRegistry.getAirmon(application, capture.airmon)
            airmon?.let {
                description = airmon.description
                rarity = airmon.rarity
                type = airmon.type
                image = airmon.image
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun releaseAirmon() {
        AirmonsApiInterfaceImplementation.deleteCapturedAirmons(application, id)
        val xP = xPRelease(rarity)
        addReleaseXP(xP)

        val context = application.applicationContext
        Toast.makeText(context, "+$releaseXP xp!", Toast.LENGTH_SHORT).show()
    }

    private fun xPRelease(rarity: AirmonRarity): Int = when (rarity) {
        AirmonRarity.UNKNOWN -> 0
        AirmonRarity.COMMON -> 5
        AirmonRarity.SPECIAL -> 10
        AirmonRarity.EPIC -> 15
        AirmonRarity.MYTHICAL -> 40
        AirmonRarity.LEGENDARY -> 200
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addReleaseXP(xp: Int) {
        if (ItemActiveRegistry.isActive("exp_booster")) {
            releaseXP = xp * 2
            PlayerRegistry.addXP(application, releaseXP.toString())
        } else {
            releaseXP = xp
            PlayerRegistry.addXP(application, releaseXP.toString())
        }
    }
}
