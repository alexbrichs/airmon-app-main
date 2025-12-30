package com.airmon.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.airmon.app.models.AirmonCollectionRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

typealias ListPair = List<Pair<Int, String>>

/**
 * @param application
 */
class CollectionViewModel(application: Application) : ViewModel() {
    private val observableListAirmons: MutableStateFlow<ListPair> = MutableStateFlow(emptyList())
    val listAirmons: StateFlow<ListPair> = observableListAirmons
    init {
        AirmonCollectionRegistry.getCollection(application)
        observableListAirmons.value = AirmonCollectionRegistry.getAllCaptures()
    }
}
