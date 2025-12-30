package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airmon.app.api.ProfileApiInterfaceImplementation
import com.airmon.app.helpers.retrieveFromDB
import com.airmon.app.helpers.storeToDB

/**
 * @property application
 */
class LoginViewModel(val application: Application) : ViewModel() {
    private val _username: MutableLiveData<String> = MutableLiveData()
    val username: LiveData<String> = _username
    private val _password: MutableLiveData<String> = MutableLiveData()
    val password: LiveData<String> = _password
    private val _loginEnable: MutableLiveData<Boolean> = MutableLiveData()
    val loginEnable: LiveData<Boolean> = _loginEnable
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _hasCredentials: MutableLiveData<Boolean> = MutableLiveData()
    val hasCredentials: LiveData<Boolean> = _hasCredentials
    private val _isLogged: MutableLiveData<Boolean> = MutableLiveData()
    val isLogged: LiveData<Boolean> = _isLogged
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage
    private val _isRegistering: MutableLiveData<Boolean> = MutableLiveData()
    val isRegistering: LiveData<Boolean> = _isRegistering

    suspend fun checkStoredCredentials() {
        _isLoading.value = true

        val username: String? = retrieveFromDB(application, "username")
        val password: String? = retrieveFromDB(application, "password")
        _username.value = username ?: ""
        _password.value = password ?: ""
        _hasCredentials.value = username != null && password != null
        _isLoading.value = false
        if (hasCredentials.value == true) {
            onLoginPressed()
        }
    }
    fun onLoginChanged(username: String, password: String) {
        _username.value = username.alphanumeric()
        _password.value = password
        _loginEnable.value = isValidUsername(username) && isValidPassword(password)
        _errorMessage.value = ""
    }

    private fun String.alphanumeric() = filter { it.isLetter() || it.isDigit() }

    private fun isValidPassword(password: String): Boolean = password.length > 0

    private fun isValidUsername(username: String): Boolean = username.length > 0

    suspend fun onLoginPressed() {
        _isLoading.value = true
        val token = ProfileApiInterfaceImplementation.login(username.value!!, password.value!!)

        token?.let { tok ->
            if (tok.startsWith("banned")) {
                val reason = tok.substring(7).replace("Reason", "Banned")
                _errorMessage.value = reason
                _loginEnable.value = true
                _isLoading.value = false
                return
            }
            storeToDB(application, "username", username.value.toString())
            storeToDB(application, "password", password.value.toString())
            storeToDB(application, "token", token)
            _isLogged.value = true
        } ?: run {
            // Error, try again
            _password.value = ""
            _errorMessage.value = "Wrong credentials, try again"
            _loginEnable.value = false
        }
        _isLoading.value = false
    }

    fun onRegisterPressed() {
        _isRegistering.value = true
        _username.value = ""
        _password.value = ""
    }

    fun showLoginScreen() {
        _isRegistering.value = false
    }
}
