package com.airmon.app.viewmodels

import androidx.compose.ui.graphics.Color
import com.airmon.app.R
import com.airmon.app.models.Pollutant
import com.airmon.app.models.StationRegistry
import kotlin.properties.Delegates

/**
 * @param stationName
 */
class StationInfoViewModel(stationName: String) {
    val name = stationName
    var icqa by Delegates.notNull<Double>()
    lateinit var date: String
    lateinit var hour: String
    lateinit var worst_pollutant: String
    lateinit var pollutants: List<Pollutant>

    init {
        val station = StationRegistry.getStation(stationName)
        station?.let {
            date = station.measure.date
            hour = station.measure.hour
            worst_pollutant = station.measure.nom_pollutant
            icqa = station.measure.icqa
            pollutants = station.measure.pollutants
        }
    }

    fun getICQAString(): Int = when (icqa) {
        1.0 -> R.string.good
        2.0 -> R.string.reasonablyGood
        3.0 -> R.string.regular
        4.0 -> R.string.unfavorable
        5.0 -> R.string.veryUnfavorable
        6.0 -> R.string.extremlyUnfavorable
        else -> R.string.error
    }

    fun getICQAColor(): Color = when (icqa) {
        1.0 -> Color(56, 161, 209)  // Bona
        2.0 -> Color(50, 161, 93)  // Raonablement bona
        3.0 -> Color(242, 229, 73)  // Regular
        4.0 -> Color(200, 52, 66)  // Desfavorable
        5.0 -> Color(109, 22, 30)  // Molt desfavorable
        6.0 -> Color(163, 91, 165)  // Extremadament desfavorable
        else -> Color(1, 1, 1, 1)  // Error
    }
}
