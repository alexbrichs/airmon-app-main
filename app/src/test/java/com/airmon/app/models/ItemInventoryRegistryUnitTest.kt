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

class ItemInventoryRegistryUnitTest {
    private val item1 = ItemInventory("Item1", 10)
    private val item2 = ItemInventory("Item2", 5)
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(ItemApiInterfaceImplementation)
        mockkObject(application)
        val map: HashMap<String, ItemInventory> = HashMap()
        map[item1.item_name] = item1
        map[item2.item_name] = item2
        every { ItemApiInterfaceImplementation.getPlayerItems(application) } returns map
        ItemInventoryRegistry.getItemsInventory(application)
    }

    @Test
    fun testGetAllItemInventory() {
        val listItems = listOf(item1.item_name, item2.item_name)
        assertEquals(listItems, ItemInventoryRegistry.getAllItemInventory())
    }

    @Test
    fun testGetItemInventory() {
        assertEquals(item1, ItemInventoryRegistry.getItemInventory(item1.item_name))
    }

    @Test
    fun testGetItemImageImage() {
        mockkObject(ItemRegistry)
        every { ItemRegistry.getItemImage(item1.item_name) } returns "image"

        assertEquals("image", ItemRegistry.getItemImage(item1.item_name))
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
