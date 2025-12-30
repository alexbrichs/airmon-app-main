package com.airmon.app.models

/**
 * @property id
 * @property name
 * @property type
 * @property description
 * @property requirement
 * @property xp
 * @property data
 */
data class Logro(
    val id: Int,
    val name: String,
    val type: String,
    val description: String,
    val requirement: Int,
    val xp: Int,
    val data: String
)
