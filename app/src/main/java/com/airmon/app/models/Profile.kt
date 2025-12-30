package com.airmon.app.models

/**
 * @property id
 * @property username
 * @property password
 * @property email
 * @property token
 */
data class Profile(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val token: String
)

/**
 * @property token
 */
data class Token(
    val token: String
)
