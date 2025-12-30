package com.airmon.app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airmon.app.R
import com.airmon.app.viewmodels.CountdownViewModel
import com.airmon.app.viewmodels.RouletteViewModel
import com.commandiron.spin_wheel_compose.SpinWheel
import com.commandiron.spin_wheel_compose.SpinWheelDefaults.spinWheelDimensions
import com.commandiron.spin_wheel_compose.state.rememberSpinWheelState
import kotlinx.coroutines.launch

fun Int.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

@Composable
fun RouletteScreen(rouletteViewModel: RouletteViewModel, navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.roulette).uppercase(),
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Roulette(
                Modifier,
                rouletteViewModel
            )
        }
    }
}

@Composable
fun Roulette(modifier: Modifier, viewModel: RouletteViewModel) {
    val canSpin: Boolean by viewModel.canSpin.observeAsState(initial = false)
    val prizes = viewModel.availablePrizes()
    val state = rememberSpinWheelState(
        pieCount = prizes.size
    )
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dimensions = spinWheelDimensions(
        spinWheelSize = screenWidth - 20.dp,
    )

    Column(
        modifier = modifier,
    ) {
        SpinWheel(
            state = state,
            dimensions = dimensions,
            onClick = {
                if (canSpin) {
                    scope.launch {
                        state.animate { pieIndex ->
                            viewModel.wonPrize(prizes[pieIndex])
                        }
                    }
                }
            }
        ) {pieIndex ->
            Column {
                val prize = prizes[pieIndex]
                val quantity = prizes[pieIndex].quantity
                Text(text = "x$quantity", fontSize = 25.sp)
                Image(imageVector = prize.type.icon, contentDescription = "Prize icon")
            }
        }
    }
    if (!canSpin) {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(stringResource(R.string.spinAgainIn))
        Countdown(viewModel = CountdownViewModel())
    }
}

@Composable
fun Countdown(viewModel: CountdownViewModel) {
    val value by viewModel.timer.collectAsState()
    viewModel.startTimer()
    Text(text = value.formatTime(), fontSize = 30.sp)
}
