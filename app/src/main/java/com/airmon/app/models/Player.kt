package com.airmon.app.models

/**
 * @property username
 * @property coins
 * @property xp_points
 * @property avatar
 */
data class Player(
    val username: String,
    val coins: Int,
    val xp_points: Int,
    val avatar: String,
)
