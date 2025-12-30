package com.airmon.app.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.StationInfoViewModel

@Composable
fun StationInfoScreen(viewModel: StationInfoViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.name,
            style = InfoTypography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 64.dp)
        )
        CircleWithICQA(viewModel)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.pollutant),
                style = InfoTypography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
        LlistarContamintants(viewModel)
    }
}

@Composable
fun CircleWithICQA(viewModel: StationInfoViewModel) {
    Box(
        modifier = Modifier
            .size(250.dp)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = viewModel.getICQAColor(),
                style = Stroke(width = 4.dp.toPx()),
                center = Offset(size.width / 2, size.height / 2),
                radius = (size.minDimension / 2)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ICQA",
                style = InfoTypography.bodyMedium,
                color = viewModel.getICQAColor()
            )
            Text(
                text = viewModel.icqa.toInt().toString(),
                style = InfoTypography.bodyMedium,
                color = viewModel.getICQAColor(),
            )
            Text(
                text = stringResource(viewModel.getICQAString()),
                style = InfoTypography.bodyMedium,
                color = viewModel.getICQAColor(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LlistarContamintants(viewModel: StationInfoViewModel) {
    val pollutants = viewModel.pollutants
    pollutants.forEach { pollutant ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "${pollutant.pollutant_name}:",
                        style = InfoTypography.bodyMedium,
                        fontSize = 25.sp
                    )
                    Text(
                        text = "${pollutant.quantity} ${pollutant.measure_unit}",
                        style = InfoTypography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}
