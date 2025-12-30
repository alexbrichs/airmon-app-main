package com.airmon.app.models

/**
 * @property sender
 * @property receiver
 * @property content
 * @property date
 * @property read
 */
data class Message(
    val sender: String,
    val receiver: String,
    val content: String,
    val date: String,
    val read: Boolean
)
