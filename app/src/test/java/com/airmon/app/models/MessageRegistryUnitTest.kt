package com.airmon.app.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MessageRegistryUnitTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val message1 = Message("User1", "User2", "Test message 1", "20/05/2024", true)
    private val message2 = Message("User1", "User2", "Test message 2", "20/05/2024", false)

    @Before
    fun setUp() {
        MessageRegistry.clearMessages()
    }

    @Test
    fun testAddMessage() {
        MessageRegistry.addMessage(message1)
        MessageRegistry.addMessage(message2)
        Assert.assertEquals(listOf(message1, message2), MessageRegistry.messages.value)
    }

    @Test
    fun testAddMessageWS() {
        MessageRegistry.addMessage(message1)
        MessageRegistry.addMessage(message2)
        Assert.assertEquals(listOf(message1, message2), MessageRegistry.messages.value)
    }

    @Test
    fun testClearMessages() {
        MessageRegistry.addMessage(message1)
        MessageRegistry.addMessage(message2)
        MessageRegistry.clearMessages()
        Assert.assertEquals(emptyList<Message>(), MessageRegistry.messages.value)
    }

    @After
    fun afterTests() {
        MessageRegistry.clearMessages()
    }
}
