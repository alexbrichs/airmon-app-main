package com.airmon.app.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airmon.app.R
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.EventInfoViewModel

@Composable
fun EventInfoScreen(viewModel: EventInfoViewModel) {
    val eventName = viewModel.name
    val espai = viewModel.espai
    val dataIni = viewModel.dataIni
    val dataFi = viewModel.dataFi

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EventName(eventName)
        EventLocation(espai)
        EventStartDate(dataIni)
        EventEndDate(dataFi)
    }
}

@Composable
private fun EventLocation(location: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = stringResource(R.string.space) + ": " + location,
            style = InfoTypography.displayMedium,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun EventStartDate(data: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = stringResource(R.string.start_date) + ": " + data,
            style = InfoTypography.displayMedium,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun EventEndDate(data: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = stringResource(R.string.end_date) + ": " + data,
            style = InfoTypography.displayMedium,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun EventName(name: String) {
    Text(
        text = name,
        style = InfoTypography.titleLarge,
        fontSize = 48.sp,
        modifier = Modifier.padding(vertical = 64.dp)
    )
}
