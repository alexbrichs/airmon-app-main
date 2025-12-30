package com.airmon.app.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airmon.app.api.SettingsApiInterfaceImplementation

/**
 * @property application
 */
class SettingsViewModel(val application: Application) : ViewModel() {
    private val _password1: MutableLiveData<String> = MutableLiveData()
    val password1: LiveData<String> = _password1
    private val _password2: MutableLiveData<String> = MutableLiveData()
    val password2: LiveData<String> = _password2
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    fun onPassword1Change(password: String) {
        _password1.value = password
    }

    fun onPassword2Change(password: String) {
        _password2.value = password
    }

    fun guardarPassword(
        password: String,
        password2: String,
        avatar: Uri?
    ) {
        if (password == password2) {
            _errorMessage.value = ""
            SettingsApiInterfaceImplementation.editUser(application, password, avatar)
        } else {
            _errorMessage.value = "Both passwords must be the same"
        }
    }
    fun uploadImage(uri: Uri) {
        // ProfileApiInterfaceImplementation.postImage(application, uri)
    }
}
