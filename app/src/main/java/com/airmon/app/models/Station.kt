package com.airmon.app.models

/**
 * @property name
 * @property longitude
 * @property latitude
 * @property measure
 */
data class Station(
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val measure: Measure,
)
