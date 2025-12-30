package com.airmon.app.helpers

import android.app.Application
import kotlinx.coroutines.runBlocking

fun getGetApiHeaders(application: Application): MutableMap<String, String> {
    var token: String?
    runBlocking { token = retrieveFromDB(application, "token") }
    val headerMap: MutableMap<String, String> = mutableMapOf()
    headerMap["Authorization"] = "Token $token"
    headerMap["Accept"] = "application/json"
    headerMap["Content-Type"] = "application/json"
    return headerMap
}

fun getPostApiHeaders(application: Application): MutableMap<String, String> {
    var token: String?
    runBlocking { token = retrieveFromDB(application, "token") }
    val headerMap: MutableMap<String, String> = mutableMapOf()
    headerMap["Authorization"] = "Token $token"
    headerMap["Accept"] = "application/json"
    return headerMap
}
