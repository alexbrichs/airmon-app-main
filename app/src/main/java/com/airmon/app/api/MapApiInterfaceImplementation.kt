package com.airmon.app.api

import android.app.Application

import com.airmon.app.helpers.getGetApiHeaders
import com.airmon.app.models.Event
import com.airmon.app.models.Station
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.runBlocking

object MapApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(MapApiInterface::class.java)

    fun getStations(application: Application): HashMap<String, Station> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Station>>
        runBlocking {
            response = apiService.getStations(headers)
        }
        val stationMap: HashMap<String, Station> = HashMap()
        if (response.isSuccessful) {
            val stations: List<Station>? = response.body()
            stations?.forEach { station ->
                stationMap[station.name] = station
            }
        }
        return stationMap
    }

    fun getEvents(application: Application): HashMap<String, Event> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Event>>
        runBlocking {
            response = apiService.getEvents(headers)
        }
        val eventMap: HashMap<String, Event> = HashMap()
        if (response.isSuccessful) {
            val events: List<Event>? = response.body()
            events?.forEach { event ->
                eventMap[event.espai] = event
            }
        }
        return eventMap
    }
}
