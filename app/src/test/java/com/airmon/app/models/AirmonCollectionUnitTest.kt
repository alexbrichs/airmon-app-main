package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.AirmonsApiInterfaceImplementation
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AirmonCollectionUnitTest {
    private val displayAirmon1 = DisplayAirmon(1, "CO", "1-1-2024 19:59:00 UTC")
    private val displayAirmon2 = DisplayAirmon(2, "CO", "1-1-2024 19:59:00 UTC")
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(AirmonsApiInterfaceImplementation)

        mockkObject(application)
        val map: HashMap<Int, DisplayAirmon> = HashMap()
        map[displayAirmon1.id] = displayAirmon1
        map[displayAirmon2.id] = displayAirmon2
        every { AirmonsApiInterfaceImplementation.getCapturedAirmons(application) } returns map
        AirmonCollectionRegistry.getCollection(application)
    }

    @Test
    fun testGetAllCaptures() {
        val listCapturedAiroms = listOf(Pair(displayAirmon1.id, displayAirmon1.airmon), Pair(displayAirmon2.id,
            displayAirmon2.airmon))
        assertEquals(listCapturedAiroms, AirmonCollectionRegistry.getAllCaptures())
    }

    @Test
    fun testGetCapture() {
        assertEquals(displayAirmon1, AirmonCollectionRegistry.getCapture(displayAirmon1.id))
    }

    @Test
    fun testCaptureAirmons() {
        every { AirmonsApiInterfaceImplementation.captureAirmon(application, 123_456) } just runs
        AirmonCollectionRegistry.captureAirmons(application, 123_456)
        verify(exactly = 1) {
            AirmonsApiInterfaceImplementation.captureAirmon(application, 123_456)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
