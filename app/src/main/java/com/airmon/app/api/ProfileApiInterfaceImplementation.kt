package com.airmon.app.api

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.airmon.app.helpers.getGetApiHeaders

import com.airmon.app.helpers.getPostApiHeaders
import com.airmon.app.models.ApiSpin
import com.airmon.app.models.ItemActive
import com.airmon.app.models.Player
import com.airmon.app.models.Token
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File

import kotlinx.coroutines.runBlocking

object ProfileApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ProfileApiInterface::class.java)

    fun login(username: String, password: String): String? {
        var response: Response<Token>
        runBlocking {
            response = apiService.login(username, password)
        }
        if (response.isSuccessful) {
            val token: Token? = response.body()

            if (token?.token?.contains("Reason") == true) {
                return "banned ${token.token}"
            }
            response.errorBody()
            return token?.token
        }

        return null
    }

    fun register(username: String, password: String): String? {
        var response: Response<Token>
        runBlocking {
            response = apiService.register(username, password)
        }
        if (response.isSuccessful) {
            val token: Token? = response.body()
            return token?.token
        }
        return null
    }

    fun postImage(application: Application, uri: Uri) {
        val file = File(requireNotNull(getRealPathFromUri(application, uri)))
        val headers = getPostApiHeaders(application)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()
        val response: Response<Void>
        runBlocking {
            response = apiService.uploadImage(headers, requestBody)
        }
        if (response.isSuccessful) {
            Log.d("PostImage", "Penjada")
        }
    }

    fun getLastSpin(application: Application): String {
        val headers = getGetApiHeaders(application)
        var response: Response<ApiSpin>
        runBlocking {
            response = apiService.getLastSpin(headers)
        }
        if (response.isSuccessful) {
            return response.body()?.last_spin ?: ""
        }
        return ""
    }

    fun postSpin(application: Application) {
        val headers = getPostApiHeaders(application)
        var response: Response<ApiSpin>
        runBlocking {
            response = apiService.postSpin(headers)
        }
        if (response.isSuccessful) {
            Log.d("", "")
        }
    }

    fun getProximLogro(application: Application, nomLogro: String): LogroProgre? {
        val headers = getPostApiHeaders(application)
        val response: Response<LogroProgre>
        runBlocking {
            response = apiService.getLogros(headers, nomLogro)
        }
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
    
    fun getPlayer(application: Application, username: String): Player? {
        val headers = getGetApiHeaders(application)
        var response: Response<Player>
        runBlocking {
            response = apiService.getPlayer(headers, username)
        }
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
    
    fun buyItem(
        application: Application,
        item: String,
        qtt: Int,
        free: Boolean
    ) {
        val headers = getPostApiHeaders(application)
        runBlocking {
            apiService.buyItem(headers, item, qtt, free)
        }
    }

    fun getActiveItems(application: Application): HashMap<String, ItemActive> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<ItemActive>>
        runBlocking {
            response = apiService.getActiveItems(headers)
        }
        val itemMap: HashMap<String, ItemActive> = HashMap()
        if (response.isSuccessful) {
            val itemsActive: List<ItemActive>? = response.body()
            itemsActive?.forEach { itemActive ->
                itemMap[itemActive.item_name] = itemActive
            }
        }
        return itemMap
    }

    fun activeItem(application: Application, item: String) {
        val headers = getPostApiHeaders(application)
        runBlocking {
            apiService.activeItem(headers, item)
        }
    }

    fun addXP(application: Application, xp: String) {
        val headers = getPostApiHeaders(application)
        runBlocking {
            apiService.addXP(headers, xp)
        }
    }

    fun addCoins(application: Application, coins: String) {
        val headers = getPostApiHeaders(application)
        runBlocking {
            apiService.addCoins(headers, coins)
        }
    }
}

fun getRealPathFromUri(application: Application, uri: Uri): String? {
    var realPath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    application.applicationContext.contentResolver.query(uri, projection, null, null, null)
        ?.use { cursor ->
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            if (columnIndex != -1) {
                realPath = cursor.getString(columnIndex)
            } else {
                realPath = uri.path
            }
        }
    return realPath
}
