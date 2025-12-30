package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.AirmonsApiInterfaceImplementation
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AirmonRegistryUnitTest {
    private val airmon1 = Airmon("CO", "monoxid de carboni", AirmonRarity.COMMON, AirmonType.NO2, "image")
    private val airmon2 = Airmon("NO", "monoxid de nitrogen", AirmonRarity.SPECIAL, AirmonType.PM25, "image")

    @Before
    fun setUp() {
        mockkObject(AirmonsApiInterfaceImplementation)
        val application = Application()
        mockkObject(application)
        val airmonMap: HashMap<String, Airmon> = HashMap()
        airmonMap[airmon1.name] = airmon1
        airmonMap[airmon2.name] = airmon2
        every { AirmonsApiInterfaceImplementation.getAirmons(application) } returns airmonMap
        AirmonRegistry.getAirmons(application)
    }
    @Test
    fun testGetAirmon() {
        val airmon = AirmonRegistry.getAirmon(airmon1.name)
        assertEquals(airmon1, airmon)
    }
    @Test
    fun getAllAirmonNames() {
        val airmonList = listOf(airmon2.name, airmon1.name)
        assertEquals(AirmonRegistry.getAllAirmonNames(), airmonList)
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
