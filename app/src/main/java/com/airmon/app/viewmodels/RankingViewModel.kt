package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.api.StatisticsApiInterfaceImplementation
import com.airmon.app.models.StationRegistry

/**
 * @property application
 */
class RankingViewModel(val application: Application) : ViewModel() {
    val statisticsApi = listOf("n_airmons_capturats",
        "airmons_alliberats",
        "n_consumibles_usats",
        "n_tirades_ruleta",
        "total_coins",
        "total_airmons_common",
        "total_airmons_special",
        "total_airmons_epic",
        "total_airmons_mythical",
        "total_airmons_legendary",
        "total_compres",
        "xp_points")

    fun getStations(query: String): List<String> =
        StationRegistry.getStationNamesOrderedByICQA(query)

    fun getStatistic(statistics: List<String>, statistic: String): String =
        when (statistic) {
            statistics[0] -> statisticsApi[0]
            statistics[1] -> statisticsApi[1]
            statistics[2] -> statisticsApi[2]
            statistics[3] -> statisticsApi[3]
            statistics[4] -> statisticsApi[4]
            statistics[5] -> statisticsApi[5]
            statistics[6] -> statisticsApi[6]
            statistics[7] -> statisticsApi[7]
            statistics[8] -> statisticsApi[8]
            statistics[9] -> statisticsApi[9]
            statistics[10] -> statisticsApi[10]
            statistics[11] -> statisticsApi[11]
            else -> "n_airmons_capturats"
        }

    fun getUsersByStatistic(statistic: String): List<Pair<String, Int>> =
        StatisticsApiInterfaceImplementation.getRankingUsers(application = application, statistic = statistic)
}
