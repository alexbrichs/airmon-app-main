package com.airmon.app.models

/**
 * @property name
 * @property description
 * @property rarity
 * @property type
 * @property image
 */
data class ApiAirmon(
    val name: String?,
    val description: String?,
    val rarity: String?,
    val type: String?,
    val image: String?
)
