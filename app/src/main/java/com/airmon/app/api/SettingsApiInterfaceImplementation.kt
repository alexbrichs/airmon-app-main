package com.airmon.app.api

import android.app.Application
import android.net.Uri
import android.util.Log

import com.airmon.app.helpers.getPostApiHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File

import kotlinx.coroutines.runBlocking

object SettingsApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(SettingsApiInterface::class.java)

    fun editUser(
        application: Application,
        password: String,
        avatar: Uri?
    ) {
        val headers = getPostApiHeaders(application)
        var requestBody: RequestBody? = null

        avatar?.let {
            val file = File(requireNotNull(getRealPathFromUri(application, avatar)))
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .addFormDataPart(
                    "avatar",
                    file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("language", "")
                .build()
        } ?: run {
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("password", password)
                .addFormDataPart("avatar", "")
                .addFormDataPart("language", "")
                .build()
        }
        val response: Response<Void>
        runBlocking {
            response = apiService.editUser(headers, requestBody)
        }
        if (response.isSuccessful) {
            Log.d("EditUsuari", "Registrat")
        }
    }
}
