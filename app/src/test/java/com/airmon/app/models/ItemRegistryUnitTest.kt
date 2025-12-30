package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ItemApiInterfaceImplementation
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ItemRegistryUnitTest {
    private val item1 = Item("Item1", 100, "L'item numero 1", "image", "00:00:50")
    private val item2 = Item("Item2", 200, "L'item numero 2", "image", "00:01:50")
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(ItemApiInterfaceImplementation)
        mockkObject(application)
        val map: HashMap<String, Item> = HashMap()
        map[item1.name] = item1
        map[item2.name] = item2
        every { ItemApiInterfaceImplementation.getItems(application) } returns map
        ItemRegistry.getItems(application)
    }

    @Test
    fun testGetAllItemNames() {
        val listItems = listOf(item1.name, item2.name)
        assertEquals(listItems, ItemRegistry.getAllItemNames())
    }

    @Test
    fun testGetItem() {
        assertEquals(item1, ItemRegistry.getItem(item1.name))
    }

    @Test
    fun testGetItemImage() {
        assertEquals(item1.image, ItemRegistry.getItemImage(item1.name))
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
