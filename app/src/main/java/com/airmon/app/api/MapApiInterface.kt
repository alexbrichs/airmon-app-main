package com.airmon.app.api

import com.airmon.app.models.Event
import com.airmon.app.models.Station
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface MapApiInterface {
    @GET("/api/map/")
    suspend fun getStations(@HeaderMap headers: Map<String, String>): Response<List<Station>>

    @GET("/api/events/")
    suspend fun getEvents(@HeaderMap headers: Map<String, String>): Response<List<Event>>
}
