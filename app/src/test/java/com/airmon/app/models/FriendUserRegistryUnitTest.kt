package com.airmon.app.models

import android.app.Application
import com.airmon.app.api.ChatApiInterfaceImplementation
import com.airmon.app.api.FriendsApiInterfaceImplementation
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FriendUserRegistryUnitTest {
    private val friend1 = FriendUser("User1", 1, "19/5/2024")
    private val friend2 = FriendUser("User2", 2, "19/5/2024")
    private var application = Application()

    @Before
    fun setUp() {
        mockkObject(FriendsApiInterfaceImplementation)
        mockkObject(ChatApiInterfaceImplementation)
        mockkObject(application)
        val friendMap: HashMap<String, FriendUser> = HashMap()
        friendMap[friend1.username] = friend1
        friendMap[friend2.username] = friend2
        every { FriendsApiInterfaceImplementation.getFriends(application) } returns friendMap
        FriendUserRegistry.getFriends(application)
    }
    @Test
    fun testGetFriend() {
        val friend = FriendUserRegistry.getFriend(friend1.username)
        Assert.assertEquals(friend1, friend)
    }
    @Test
    fun testGetAllFriendNames() {
        val airmonList = listOf(friend2.username, friend1.username)
        Assert.assertEquals(airmonList, FriendUserRegistry.getAllFriendNames())
    }
    @Test
    fun testGetChat() {
        val chat = listOf(Message("user1", friend1.username, "Hello", "19/5/2024", false))
        every {
            ChatApiInterfaceImplementation.getChat(
                application, 1)
        } returns chat
        Assert.assertEquals(chat, FriendUserRegistry.getChat(application, friend1.username))
    }

    @Test
    fun testGetLastMessage() {
        val message = Message("user1", friend1.username, "Hello", "19/5/2024", false)
        every {
            ChatApiInterfaceImplementation.getLastMessage(
                application, 1
            )
        } returns message
        Assert.assertEquals(message, FriendUserRegistry.getLastMessage(application, friend1.username))
    }

    @Test
    fun TestGetUsers() {
        val result = mutableListOf("User1")
        every {
            FriendsApiInterfaceImplementation.find_user(application, "User1")
        } returns result
        Assert.assertEquals(result, FriendUserRegistry.getUsers(application, "User1"))
    }

    @Test
    fun testIsFriend() {
        Assert.assertEquals(true, FriendUserRegistry.isFriend(friend1.username))
    }

    @Test
    fun testIsNotFriend() {
        Assert.assertEquals(false, FriendUserRegistry.isFriend("User3"))
    }

    @Test
    fun testAddFriend() {
        every {
            FriendsApiInterfaceImplementation.addFriend(application, "User3")
        } returns 3
        FriendUserRegistry.addFriend(application, "User3")
        val friends = FriendUserRegistry.getAllFriendNames()
        Assert.assertTrue(friends.contains("User3"))
    }

    @Test
    fun testUnFriend() {
        every { FriendsApiInterfaceImplementation.unFriend(application, "User2") } just runs
        FriendUserRegistry.unFriend(application, "User2")
        val friends = FriendUserRegistry.getAllFriendNames()
        Assert.assertFalse(friends.contains("User2"))
        verify(exactly = 1) {
            FriendsApiInterfaceImplementation.unFriend(application, "User2")
        }
    }
    @Test
    fun testGetFotos() {
        val fotos = listOf("foto1", "foto2")
        every {
            FriendsApiInterfaceImplementation.getFotos(application, "User1")
        } returns fotos
        Assert.assertEquals(fotos, FriendUserRegistry.getFotos(application, "User1"))
    }

    @After
    fun afterTests() {
        unmockkAll()
    }
}
