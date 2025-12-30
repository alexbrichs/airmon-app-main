package com.airmon.app.api

import android.app.Application
import android.widget.Toast

import com.airmon.app.R
import com.airmon.app.helpers.getGetApiHeaders
import com.airmon.app.helpers.getPostApiHeaders
import com.airmon.app.models.Airmon
import com.airmon.app.models.AirmonRarity
import com.airmon.app.models.AirmonType
import com.airmon.app.models.ApiAirmon
import com.airmon.app.models.DisplayAirmon
import com.airmon.app.models.SpawnedAirmon
import com.airmon.app.models.SpawnedList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.runBlocking

object AirmonsApiInterfaceImplementation {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.21.149.211")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(AirmonsApiInterface::class.java)

    fun getAirmons(application: Application): HashMap<String, Airmon> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<Airmon>>
        runBlocking {
            response = apiService.getAirmons(headers)
        }
        val airmonMap: HashMap<String, Airmon> = HashMap()
        if (response.isSuccessful) {
            val airmons: List<Airmon>? = response.body()
            airmons?.forEach { airmon ->
                airmonMap[airmon.name] = airmon
            }
        }
        return airmonMap
    }

    fun getAirmon(application: Application, name: String): Airmon? {
        val headers = getGetApiHeaders(application)
        var response: Response<ApiAirmon>
        runBlocking {
            response = apiService.getAirmon(headers, name)
        }
        if (response.isSuccessful) {
            return airmonFromApi(response.body())
        }
        return null
    }

    fun getNearbyAirmons(
        application: Application,
        latitude: Double,
        longitude: Double
    ): HashMap<Int, SpawnedAirmon> {
        val headers = getGetApiHeaders(application)
        var response: Response<SpawnedList>
        runBlocking {
            response = apiService.getNearbyAirmons(headers, latitude.toString(), longitude.toString())
        }
        val airmonMap: HashMap<Int, SpawnedAirmon> = HashMap()
        if (response.isSuccessful) {
            val airmons: List<SpawnedAirmon>? = response.body()?.airmons
            airmons?.forEach { airmon ->
                airmonMap[airmon.spawned_airmon_id] = airmon
            }
        }
        return airmonMap
    }

    fun getCapturedAirmons(application: Application): HashMap<Int, DisplayAirmon> {
        val headers = getGetApiHeaders(application)
        var response: Response<List<DisplayAirmon>>
        runBlocking {
            response = apiService.getCaptures(headers)
        }
        val collection: HashMap<Int, DisplayAirmon> = HashMap()
        if (response.isSuccessful) {
            val captures: List<DisplayAirmon>? = response.body()
            captures?.forEach { capture ->
                collection[capture.id] = capture
            }
        }
        return collection
    }

    fun captureAirmon(application: Application, spawnedAirmonId: Int) {
        val headers = getPostApiHeaders(application)
        var response: Response<DisplayAirmon>
        runBlocking {
            response = apiService.captureAirmon(headers, spawnedAirmonId)
        }
        if (response.isSuccessful) {
            val displayAirmon: DisplayAirmon? = response.body()
            val context = application.applicationContext
            val string = context.getString(R.string.youCaptured, displayAirmon?.airmon)
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteCapturedAirmons(application: Application, id: Int) {
        val headers = getGetApiHeaders(application)
        var response: Response<Void>
        runBlocking {
            response = apiService.deleteCapturedAirmon(headers, id)
        }
        if (response.isSuccessful) {
            val string = application.getString(R.string.released).uppercase()
            Toast.makeText(application, string, Toast.LENGTH_SHORT).show()
        }
    }
}

fun airmonFromApi(apiAirmon: ApiAirmon?): Airmon? {
    val name = apiAirmon?.name ?: ""
    val description = apiAirmon?.description ?: ""
    val rarity = apiAirmon?.rarity?.let {
        AirmonRarity.valueOf(apiAirmon.rarity.uppercase())
    } ?: AirmonRarity
        .UNKNOWN
    val type = apiAirmon?.type?.let {
        AirmonType.valueOf(apiAirmon.type.uppercase())
    } ?: AirmonType.UNKNOWN
    val image = apiAirmon?.image ?: ""
    return Airmon(name, description, rarity, type, image)
}
