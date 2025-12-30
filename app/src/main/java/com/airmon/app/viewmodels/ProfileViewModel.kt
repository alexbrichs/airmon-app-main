package com.airmon.app.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

import com.airmon.app.LoginActivity
import com.airmon.app.api.ProfileApiInterfaceImplementation
import com.airmon.app.helpers.removeFromDB
import com.airmon.app.models.Estadistiques
import com.airmon.app.models.FriendUserRegistry
import com.airmon.app.models.ItemRegistry
import com.airmon.app.models.Logro

import kotlinx.coroutines.runBlocking

/**
 * @param userName
 * @property application
 */
class ProfileViewModel(val application: Application, userName: String) : ViewModel() {
    val name = userName

    init {
        ItemRegistry.getItems(application)
    }
    fun onLogoutPressed() {
        runBlocking {
            removeFromDB(application, "token")
            removeFromDB(application, "username")
            removeFromDB(application, "password")
            removeFromDB(application, "lastSpin")
        }
        // Change to login activity
        val intent = Intent(application.applicationContext, LoginActivity::class.java)
        application.startActivity(intent)
    }

    fun uploadImage(uri: Uri) {
        ProfileApiInterfaceImplementation.postImage(application, uri)
    }

    fun getAvatar(username: String): String? =
        FriendUserRegistry.getAvatar(application, username)

    fun getEstadistiques(username: String): Estadistiques? =
        FriendUserRegistry.getEstadistiques(application, username)

    fun getLogros(username: String): List<Logro>? =
        FriendUserRegistry.getLogros(application, username)
    fun obteExp(username: String): Int? =
        FriendUserRegistry.getExp(application, username)
}
