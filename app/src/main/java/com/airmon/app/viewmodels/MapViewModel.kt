package com.airmon.app.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.airmon.app.R

import com.airmon.app.models.AirmonCollectionRegistry
import com.airmon.app.models.AirmonRarity
import com.airmon.app.models.AirmonRegistry
import com.airmon.app.models.Event
import com.airmon.app.models.EventRegistry
import com.airmon.app.models.ItemActiveRegistry
import com.airmon.app.models.PlayerRegistry
import com.airmon.app.models.SpawnedAirmon
import com.airmon.app.models.Station
import com.airmon.app.models.StationRegistry
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

/**
 * @param application
 */
class MapViewModel(@get:JvmName("getMVMContext") private val application: Application) :
    AndroidViewModel(application) {
    var nearest_station_distance: Double = Double.MAX_VALUE
    var nearest_station: MutableLiveData<Station?> = MutableLiveData()
    private val geolocation: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    val currentLocation: MutableLiveData<LatLng> = MutableLiveData()
    private var captureCoins: Int = 0
    private var captureXP: Int = 0
    var markersMap: HashMap<Int, SpawnedAirmon> = HashMap()
    var markersSet: HashSet<Int> = HashSet()

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 30000).apply {
                setWaitForAccurateLocation(true)
            }.build()

        // Retorna la geolocalització cada 10 segons
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    currentLocation.postValue(currentLatLng)
                    getNearbyAirmons(it.latitude, it.longitude)
                }
            }
        }
        geolocation.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun getNearbyAirmons(latitude: Double, longitude: Double): HashMap<Int, SpawnedAirmon> {
        markersMap = AirmonRegistry.getNearbyAirmons(application, latitude, longitude)
        return markersMap
    }

    fun captureAirmon(
        spawnedId: Int?,
    ) {
        spawnedId?.let {
            AirmonCollectionRegistry.captureAirmons(application, spawnedId)
            AirmonCollectionRegistry.getCollection(application)
        }
    }

    fun getAllStations(): List<String> = StationRegistry.getAllStationNames()

    fun getStation(name: String): Station? = StationRegistry.getStation(name)

    fun getAllEvents(): List<String> = EventRegistry.getAllEventNames()

    fun getEvent(name: String): Event? = EventRegistry.getEvent(name)

    fun getStationsInPolygon(polygonPoints: List<LatLng?>): List<String> {
        val stationsInPolygon: MutableList<String> = mutableListOf()
        val allStations = getAllStations()
        allStations.forEach { stationName ->
            val station = getStation(stationName)
            if (station != null &&
                    PolyUtil.containsLocation(
                        station.latitude,
                        station.longitude,
                        polygonPoints.filterNotNull(),
                        false
                    )
            ) {
                stationsInPolygon.add(station.name)
            }
        }
        return stationsInPolygon
    }

    fun getEventsInPolygon(polygonPoints: List<LatLng?>): List<String> {
        val eventsInPolygon: MutableList<String> = mutableListOf()
        val allEvents = getAllEvents()
        allEvents.forEach { eventName ->
            val event = getEvent(eventName)
            if (event != null &&
                    PolyUtil.containsLocation(
                        event.latitude,
                        event.longitude,
                        polygonPoints.filterNotNull(),
                        false
                    )
            ) {
                eventsInPolygon.add(event.espai)
            }
        }
        return eventsInPolygon
    }

    fun getAirmonFromMarker(spawnedId: Int): SpawnedAirmon? = markersMap[spawnedId]

    fun convertToColor(valColor: Double): Int = when (valColor) {
        1.0 -> Color.argb(125, 56, 161, 209)  // Bona
        2.0 -> Color.argb(125, 50, 161, 93)  // Raonablement bona
        3.0 -> Color.argb(125, 242, 229, 73)  // Regular
        4.0 -> Color.argb(125, 200, 52, 66)  // Desfavorable
        5.0 -> Color.argb(125, 109, 22, 30)  // Molt desfavorable
        6.0 -> Color.argb(125, 163, 91, 165)  // Extremadament desfavorable
        else -> Color.BLACK  // Error
    }

    fun text_station(station: Station?): Int = when (station?.measure?.icqa) {
        1.0 -> R.string.good
        2.0 -> R.string.reasonablyGood
        3.0 -> R.string.regular
        4.0 -> R.string.unfavorable
        5.0 -> R.string.veryUnfavorable
        6.0 -> R.string.extremlyUnfavorable
        else -> R.string.good
    }

    fun xPperRarity(rarity: AirmonRarity): Int = when (rarity) {
        AirmonRarity.UNKNOWN -> 0
        AirmonRarity.COMMON -> 10
        AirmonRarity.SPECIAL -> 15
        AirmonRarity.EPIC -> 20
        AirmonRarity.MYTHICAL -> 75
        AirmonRarity.LEGENDARY -> 400
    }

    fun coinsperRarity(rarity: AirmonRarity): Int = when (rarity) {
        AirmonRarity.UNKNOWN -> 0
        AirmonRarity.COMMON -> 5
        AirmonRarity.SPECIAL -> 15
        AirmonRarity.EPIC -> 20
        AirmonRarity.MYTHICAL -> 70
        AirmonRarity.LEGENDARY -> 300
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addXP(xp: Int) {
        if (ItemActiveRegistry.isActive("exp_booster")) {
            captureXP = xp * 2
            PlayerRegistry.addXP(application, captureXP.toString())
        } else {
            captureXP = xp
            PlayerRegistry.addXP(application, captureXP.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCoins(coins: Int) {
        if (ItemActiveRegistry.isActive("coin_booster")) {
            captureCoins = coins * 2
            PlayerRegistry.addCoins(application, captureCoins.toString())
        } else {
            captureCoins = coins
            PlayerRegistry.addCoins(application, captureCoins.toString())
        }
    }

    fun getAirmonRarity(airmon: SpawnedAirmon?): AirmonRarity? {
        airmon?.let {
            return AirmonRegistry.getAirmon(application, airmon.name)?.rarity
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addXPandCoins(rarity: AirmonRarity) {
        val xp = xPperRarity(rarity)
        val coins = coinsperRarity(rarity)
        addXP(xp)
        addCoins(coins)

        val context = application.applicationContext
        Toast.makeText(
            context,
            context.getString(
                R.string.earnedXPandCoins,
                captureXP.toString(),
                captureCoins.toString()
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
}
