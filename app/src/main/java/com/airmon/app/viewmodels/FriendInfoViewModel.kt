package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.models.Estadistiques
import com.airmon.app.models.FriendUserRegistry
import com.airmon.app.models.Logro

/**
 * @param friendName
 * @param application
 */
class FriendInfoViewModel(application: Application, friendName: String) : ViewModel() {
    val name = friendName
    val app = application
    fun isFriends(username: String): Boolean =
        FriendUserRegistry.isFriend(username)

    fun getAvatar(username: String): String? =
        FriendUserRegistry.getAvatar(app, username)

    fun getEstadistiques(username: String): Estadistiques? =
        FriendUserRegistry.getEstadistiques(app, username)
    fun getLogros(username: String): List<Logro>? =
        FriendUserRegistry.getLogros(app, username)

    fun obteExp(username: String): Int? =
        FriendUserRegistry.getExp(app, username)
}
