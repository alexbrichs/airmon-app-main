package com.airmon.app.compose

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController

import com.airmon.app.R
import com.airmon.app.models.AirmonRegistry
import com.airmon.app.models.SpawnedAirmon
import com.airmon.app.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("PotentialBehaviorOverride")
private fun MapView.configureGoogleMap(
    googleMap: GoogleMap,
    mapStyleOptions: MapStyleOptions,
    currentLatLng: LatLng,
    viewModel: MapViewModel,
    navController: NavController
) {
    viewModel.markersSet.clear()
    googleMap.setMapStyle(mapStyleOptions)

    val radius = 500
    val zoomLevel1 = 16 - ln(radius / 500.0) / ln(2.0)
    googleMap.setMinZoomPreference(zoomLevel1.toFloat())
    googleMap.setMaxZoomPreference(zoomLevel1.toFloat())

    googleMap.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
            currentLatLng,
            zoomLevel1.toFloat()
        )
    )

    if (ActivityCompat.checkSelfPermission(
        this.context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this.context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    googleMap.isMyLocationEnabled = true

    googleMap.uiSettings.isScrollGesturesEnabled = false
    googleMap.uiSettings.isZoomControlsEnabled = false
    googleMap.uiSettings.isZoomGesturesEnabled = false
    googleMap.uiSettings.isTiltGesturesEnabled = false
    googleMap.uiSettings.isMapToolbarEnabled = false
    googleMap.uiSettings.isCompassEnabled = false
    googleMap.uiSettings.isIndoorLevelPickerEnabled = false
    googleMap.uiSettings.isRotateGesturesEnabled = false
    googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
    googleMap.uiSettings.isMyLocationButtonEnabled = false

    googleMap.setOnMarkerClickListener { marker ->
        if (marker.position != currentLatLng && marker.tag == "Station") {
            navController.navigate("StationInfo/${marker.title}")
            true
        } else if (marker.position != currentLatLng && marker.tag == "Event") {
            navController.navigate("EventInfo/${marker.title}")
            true
        } else if (marker.position != currentLatLng && marker.tag == "Airmon") {
            val spawnedId = marker.title?.toIntOrNull()
            spawnedId?.let {
                viewModel.captureAirmon(spawnedId)
                marker.remove()
                val airmon = viewModel.getAirmonFromMarker(spawnedId)
                val rarity = viewModel.getAirmonRarity(airmon)
                rarity?.let {
                    viewModel.addXPandCoins(rarity)
                }
            }
            true
        } else {
            false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(viewModel: MapViewModel, navController: NavController) {
    val currentLatLng by viewModel.currentLocation.observeAsState()
    val nearestStation by viewModel.nearest_station.observeAsState()

    LaunchedEffect(key1 = true) {
        viewModel.updateLocation()
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        // Comprova si la ubicació actual es vàlida
        currentLatLng?.let { MapViewContainer(viewModel, it, navController) }
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(30.dp))
                .background(ComposeColor.White)
        ) {
            Text(
                text = stringResource(R.string.airQuality) +
                        ": " +
                        stringResource(viewModel.text_station(nearestStation)),
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 7.dp, start = 15.dp, end = 15.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun calculateDistance(point1: LatLng, point2: LatLng): Double {
    val latDiff = Math.toRadians(point2.latitude - point1.latitude)
    val lonDiff = Math.toRadians(point2.longitude - point1.longitude)
    val lat1 = Math.toRadians(point1.latitude)
    val lat2 = Math.toRadians(point2.latitude)

    val aa = sin(latDiff / 2) * sin(latDiff / 2) +
            sin(lonDiff / 2) * sin(lonDiff / 2) * cos(lat1) * cos(lat2)
    val cc = 2 * atan2(sqrt(aa), sqrt(1 - aa))
    val earthRadius = 6_371_000.0
    return earthRadius * cc
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("PotentialBehaviorOverride")
@Composable
fun MapViewContainer(
    viewModel: MapViewModel,
    currentLatLng: LatLng,
    navController: NavController
) {
    val context = LocalContext.current
    val googleMapState = remember { mutableStateOf<GoogleMap?>(null) }
    val polygonState = remember { mutableStateOf<Polygon?>(null) }
    val circles = remember { mutableListOf<Circle?>(null) }
    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
            getMapAsync { googleMap ->
                googleMapState.value = googleMap
                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
                configureGoogleMap(
                    googleMap,
                    mapStyleOptions,
                    currentLatLng,
                    viewModel,
                    navController
                )
            }
        }
    }

    LaunchedEffect(currentLatLng) {
        userMoved(
            googleMapState,
            currentLatLng,
            polygonState,
            circles,
            viewModel,
            context
        )
    }

    AndroidView({ mapView }) { it.onResume() }
}

private fun userMoved(
    googleMapState: MutableState<GoogleMap?>,
    currentLatLng: LatLng,
    polygonState: MutableState<Polygon?>,
    circles: MutableList<Circle?>,
    viewModel: MapViewModel,
    context: Context
) {
    googleMapState.value?.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
    polygonState.value?.remove()
    for (circle in circles) {
        circle?.remove()
    }
    viewModel.nearest_station_distance = Double.MAX_VALUE
    viewModel.nearest_station.value = null
    val onCameraIdleListener = GoogleMap.OnCameraIdleListener {
        listener(googleMapState, polygonState, viewModel, circles, currentLatLng, context)
    }
    googleMapState.value?.setOnCameraIdleListener(onCameraIdleListener)
}

private fun listener(
    googleMapState: MutableState<GoogleMap?>,
    polygonState: MutableState<Polygon?>,
    viewModel: MapViewModel,
    circles: MutableList<Circle?>,
    currentLatLng: LatLng,
    context: Context
) {
    val bounds = googleMapState.value?.projection?.visibleRegion?.latLngBounds

    val polygonPoints = getPolygonPoints(bounds)

    polygonState.value = googleMapState.value?.addPolygon(
        PolygonOptions()
            .addAll(polygonPoints)
            .strokeColor(Color.TRANSPARENT)
    )

    val stationsPolygon = viewModel.getStationsInPolygon(polygonPoints)
    stationsPolygon.forEach { stationName ->
        showStation(viewModel, stationName, googleMapState, circles, currentLatLng)
    }

    val spawnedAirmons = viewModel.getNearbyAirmons(currentLatLng.latitude, currentLatLng.longitude)
    spawnedAirmons.values.forEach { spawnedAirmon ->
        if (!viewModel.markersSet.contains(spawnedAirmon.spawned_airmon_id)) {
            showAirmon(viewModel, spawnedAirmon, context, googleMapState)
            viewModel.markersSet.add(spawnedAirmon.spawned_airmon_id)
        }
    }

    val eventsPolygon = viewModel.getEventsInPolygon(polygonPoints)
    eventsPolygon.forEach { eventName ->
        showEvent(viewModel, eventName, googleMapState)
    }
    googleMapState.value?.setOnCameraIdleListener(null)
}

private fun getPolygonPoints(bounds: LatLngBounds?) = listOf(
    bounds?.southwest?.latitude?.let { lat ->
        bounds.southwest.longitude.let { it1 ->
            LatLng(
                lat,
                it1
            )
        }
    },
    bounds?.northeast?.latitude?.let { lat ->
        bounds.southwest.longitude.let { it1 ->
            LatLng(
                lat,
                it1
            )
        }
    },
    bounds?.northeast?.latitude?.let { lat ->
        bounds.northeast.longitude.let { it1 ->
            LatLng(
                lat,
                it1
            )
        }
    },
    bounds?.southwest?.latitude?.let { lat ->
        bounds.northeast.longitude.let { it1 ->
            LatLng(
                lat,
                it1
            )
        }
    }
)

private fun showStation(
    viewModel: MapViewModel,
    stationName: String,
    googleMapState: MutableState<GoogleMap?>,
    circles: MutableList<Circle?>,
    currentLatLng: LatLng
) {
    val station = viewModel.getStation(stationName)
    station?.let { st ->
        val color = viewModel.convertToColor(st.measure.icqa)
        googleMapState.value?.addMarker(
            MarkerOptions()
                .position(LatLng(st.latitude, st.longitude))
                .title(st.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )?.tag = "Station"

        if (st.measure.icqa > 1.0) {
            val circleOptions = CircleOptions()
                .center(LatLng(st.latitude, st.longitude))
                .radius(1000.0)
                .fillColor(color)
                .strokeColor(Color.TRANSPARENT)
            val circle = googleMapState.value?.addCircle(circleOptions)
            circles.add(circle)
            val distance =
                calculateDistance(currentLatLng, LatLng(st.latitude, st.longitude))
            if (circle != null &&
                    distance <= circle.radius &&
                    distance < viewModel.nearest_station_distance
            ) {
                viewModel.nearest_station_distance = distance
                viewModel.nearest_station.value = st
            }
        }
    }
}

private fun showEvent(
    viewModel: MapViewModel,
    eventName: String,
    googleMapState: MutableState<GoogleMap?>,
) {
    val event = viewModel.getEvent(eventName)
    event?.let { ev ->
        googleMapState.value?.addMarker(
            MarkerOptions()
                .position(LatLng(ev.latitude, ev.longitude))
                .title(ev.espai)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )?.tag = "Event"
    }
}

private fun showAirmon(
    viewModel: MapViewModel,
    airmon: SpawnedAirmon,
    context: Context,
    googleMapState: MutableState<GoogleMap?>,
) {
    var bitmap = AirmonRegistry.getBitmap(airmon.name)
    bitmap ?: run {
        val imageId = context.resources.getIdentifier(
            airmon.name.lowercase(),
            "raw",
            context.packageName
        )
        bitmap = BitmapFactory.decodeResource(context.resources, imageId)
        AirmonRegistry.addBitmap(airmon.name, bitmap)
    }
    addAirmonToMap(bitmap, googleMapState, airmon)
}

private fun addAirmonToMap(
    bitmap: Bitmap?,
    googleMapState: MutableState<GoogleMap?>,
    airmon: SpawnedAirmon
) {
    bitmap ?: return
    val position = LatLng(
        airmon.location.latitude,
        airmon.location.longitude
    )
    if (googleMapState.value?.projection?.visibleRegion?.latLngBounds?.contains(position) != true) {
        return
    }
    val smallIcon = createSmallIcon(bitmap)

    googleMapState.value?.addMarker(
        MarkerOptions()
            .position(position)
            .title(airmon.spawned_airmon_id.toString())
            .icon(smallIcon)
    )?.tag = "Airmon"
}

private fun createSmallIcon(bitmap: Bitmap): BitmapDescriptor {
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 120, false)
    val composeBitmap = scaledBitmap.asImageBitmap()
    val androidBitmap = composeBitmap.asAndroidBitmap()
    return BitmapDescriptorFactory.fromBitmap(androidBitmap)
}
