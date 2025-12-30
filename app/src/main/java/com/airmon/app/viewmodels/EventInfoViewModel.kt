package com.airmon.app.viewmodels

import androidx.lifecycle.ViewModel
import com.airmon.app.models.EventRegistry
import com.airmon.app.utils.formatDateForDisplay

/**
 * @param eventName
 */
class EventInfoViewModel(eventName: String) : ViewModel() {
    val name = eventName
    var dataIni = ""
    var dataFi = ""
    var espai = ""

    init {
        val event = EventRegistry.getEvent(eventName)
        event?.let {
            dataIni = formatDateForDisplay(event.data_ini)
            dataFi = formatDateForDisplay(event.data_fi)
            espai = event.espai
        }
    }
}
