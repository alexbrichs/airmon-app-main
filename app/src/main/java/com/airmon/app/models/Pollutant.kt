package com.airmon.app.models

/**
 * @property pollutant_name
 * @property measure_unit
 * @property quantity
 */
data class Pollutant(
    val pollutant_name: String,
    val measure_unit: String,
    val quantity: Double,
)
