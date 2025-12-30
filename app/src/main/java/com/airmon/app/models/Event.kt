package com.airmon.app.models

/**
 * @property denominacio
 * @property data_ini
 * @property data_fi
 * @property longitude
 * @property latitude
 * @property espai
 */
data class Event(
    val denominacio: String,
    val data_ini: String,
    val data_fi: String,
    val longitude: Double,
    val latitude: Double,
    val espai: String
)
