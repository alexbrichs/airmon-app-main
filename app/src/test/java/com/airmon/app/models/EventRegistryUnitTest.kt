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

class EventRegistryUnitTest {
    private val event1 = Event("Event1", "27/5/2024 8:00:00 UTC", "28/5/2024 2:00:00 UTC", 1.0, 1.0, "espai1")
    private val event2 = Event("Event2", "29/5/2024 8:00:00 UTC", "31/5/2024 2:00:00 UTC", 2.0, 2.0, "espai2")
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(MapApiInterfaceImplementation)
        mockkObject(application)
        val map: HashMap<String, Event> = HashMap()
        map[event1.denominacio] = event1
        map[event2.denominacio] = event2
        every { MapApiInterfaceImplementation.getEvents(application) } returns map
        EventRegistry.getEvents(application)
    }

    @Test
    fun testGetAllEventNames() {
        val listEvents = listOf(event2.denominacio, event1.denominacio)
        assertEquals(listEvents, EventRegistry.getAllEventNames())
    }

    @Test
    fun testGetEvent() {
        assertEquals(event1, EventRegistry.getEvent(event1.denominacio))
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
