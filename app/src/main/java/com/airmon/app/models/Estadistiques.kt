package com.airmon.app.models

/**
 * @property n_airmons_capturats
 * @property airmons_alliberats
 * @property n_consumibles_usats
 * @property n_tirades_ruleta
 * @property total_coins
 * @property total_airmons_common
 * @property total_airmons_special
 * @property total_airmons_epic
 * @property total_airmons_mythical
 * @property total_airmons_legendary
 * @property total_compres
 */
data class Estadistiques(
    val n_airmons_capturats: Int,
    val airmons_alliberats: Int,
    val n_consumibles_usats: Int,
    val n_tirades_ruleta: Int,
    val total_coins: Int,
    val total_airmons_common: Int,
    val total_airmons_special: Int,
    val total_airmons_epic: Int,
    val total_airmons_mythical: Int,
    val total_airmons_legendary: Int,
    val total_compres: Int

)
