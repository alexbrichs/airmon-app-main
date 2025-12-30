package com.airmon.app.models

/**
 * @property name
 * @property price
 * @property description
 * @property image
 * @property duration
 */
data class Item(
    val name: String,
    val price: Int,
    val description: String,
    val image: String,
    val duration: String,
)
