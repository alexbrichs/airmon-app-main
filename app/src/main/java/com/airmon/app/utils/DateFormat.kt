package com.airmon.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun formatDate(inputDate: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return sdf.format(inputDate)
}

fun formatDateForDisplay(inputDate: String): String {
    val components = inputDate.split("[-T:]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val year = components[0]
    val month = components[1]
    val day = components[2]
    val hour = components[3]
    val minute = components[4]
    return "$day/$month/$year $hour:$minute"
}
