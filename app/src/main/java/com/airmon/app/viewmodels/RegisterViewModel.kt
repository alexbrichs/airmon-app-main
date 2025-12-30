package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airmon.app.R
import com.airmon.app.api.ProfileApiInterfaceImplementation
import com.airmon.app.helpers.storeToDB

/**
 * @property application
 */
class RegisterViewModel(val application: Application) : ViewModel() {
    private val _username: MutableLiveData<String> = MutableLiveData()
    val username: LiveData<String> = _username
    private val _password1: MutableLiveData<String> = MutableLiveData()
    val password1: LiveData<String> = _password1
    private val _password2: MutableLiveData<String> = MutableLiveData()
    val password2: LiveData<String> = _password2
    private val _buttonEnable: MutableLiveData<Boolean> = MutableLiveData()
    val buttonEnable: LiveData<Boolean> = _buttonEnable
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isLogged: MutableLiveData<Boolean> = MutableLiveData()
    val isLogged: LiveData<Boolean> = _isLogged
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    fun onLoginChanged(
        username: String,
        password1: String,
        password2: String
    ) {
        _username.value = username.alphanumeric()
        _password1.value = password1
        _password2.value = password2
        _buttonEnable.value = isValidUsername(username) && isValidPassword(password1) && password1 == password2
        _errorMessage.value = ""
        if (password1.isNotEmpty() &&
                password2.isNotEmpty() &&
                password1 != password2) {
            _errorMessage.value = application.applicationContext.resources.getString(R.string.passwordsDoNotMatch)
        }
    }

    private fun String.alphanumeric() = filter { it.isLetter() || it.isDigit() }

    private fun isValidPassword(password: String): Boolean = password.length > 0

    private fun isValidUsername(username: String): Boolean = username.length > 0

    suspend fun onRegisterPressed() {
        _isLoading.value = true
        val token = ProfileApiInterfaceImplementation.register(username.value!!, password1.value!!)
        token?.let {
            storeToDB(application, "username", username.value.toString())
            storeToDB(application, "password", password1.value.toString())
            storeToDB(application, "token", token)
            _isLogged.value = true
        } ?: run {
            // Error, try again
            _username.value = ""
            _password1.value = ""
            _password2.value = ""
            _errorMessage.value = application.applicationContext.resources.getString(R.string.usernameInvalid)
            _buttonEnable.value = false
        }
        _isLoading.value = false
    }
}
