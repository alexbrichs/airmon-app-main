package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ProfileApiInterfaceImplementation
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

class PlayerRegistryUnitTest {
    private val player1 = Player("Player1", 1000, 20000, "image")
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(ProfileApiInterfaceImplementation)
        mockkObject(application)
    }

    @Test
    fun testGetPlayer() {
        every { ProfileApiInterfaceImplementation.getPlayer(application, player1.username) } returns player1
        assertEquals(player1, PlayerRegistry.getPlayer(application, player1.username))
    }

    @Test
    fun testBuyItem() {
        every { ProfileApiInterfaceImplementation.buyItem(application, "item1", 1, false) } just runs
        PlayerRegistry.buyItem(application, "item1", 1)
        verify(exactly = 1) {
            ProfileApiInterfaceImplementation.buyItem(application, "item1", 1, false)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
