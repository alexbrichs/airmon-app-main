package com.airmon.app.models

/**
 * @property date
 * @property hour
 * @property icqa
 * @property nom_pollutant
 * @property pollutants
 */
data class Measure(
    val date: String,
    val hour: String,
    val icqa: Double,
    val nom_pollutant: String,
    val pollutants: List<Pollutant>,
)
