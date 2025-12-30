package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.MapApiInterfaceImplementation
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StationRegistryUnitTest {
    private val station1 = Station("Palau Reial", 1.0, 1.0, Measure("1-1-2020", "19:30:00 UTC+2", 6.0, "CO", listOf(
        Pollutant(
            "CO", "mg/m^3", 50.0
        )
    )))
    private val station2 = Station("Eixample", 2.0, 2.0, Measure("1-1-2020", "19:30:00 UTC+2", 5.0, "CO", listOf(
        Pollutant(
            "CO", "mg/m^3", 50.0
        )
    )))

    @Before
    fun setUp() {
        mockkObject(MapApiInterfaceImplementation)
        val application = Application()
        mockkObject(application)
        val stationMap: HashMap<String, Station> = HashMap()
        stationMap[station1.name] = station1
        stationMap[station2.name] = station2
        every { MapApiInterfaceImplementation.getStations(application) } returns stationMap
        StationRegistry.getStations(application)
    }

    @Test
    fun testGetStation() {
        val station = StationRegistry.getStation(station1.name)
        assertEquals(station1, station)
    }

    @Test
    fun testGetAllStationNames() {
        val stationList = listOf(station1.name, station2.name)
        assertEquals(StationRegistry.getAllStationNames(), stationList)
    }

    @Test
    fun testGetStationNamesOrderedByICQA() {
        val stationList = listOf(station2.name, station1.name)
        assertEquals(StationRegistry.getStationNamesOrderedByICQA(""), stationList)
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
