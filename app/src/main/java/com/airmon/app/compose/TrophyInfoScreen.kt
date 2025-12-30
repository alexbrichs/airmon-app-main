package com.airmon.app.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.api.LogroProgre
import com.airmon.app.viewmodels.TrophyInfoViewModel

@Composable
fun TrophyInfoScreen(
    viewModel: TrophyInfoViewModel,
    navController: NavController,
    context: Context
) {
    val logro = viewModel.getProximLogro()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .padding(bottom = 7.dp)
        ) {
            estrelles(logro)
        }
        Text(
            text = getStringByName(context, viewModel.name)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = getStringByName(context, logro!!.description)
        )

        if (logro.progress > logro.requirement) {
            Row(modifier = Modifier.padding(vertical = 20.dp)) {
                Text(
                    text = stringResource(R.string.logroAcabat)
                )
            }
        } else {
            Row(modifier = Modifier.padding(vertical = 20.dp)) {
                Text(
                    text = stringResource(R.string.bounty)
                )
                Text(
                    text = "${(logro.xp)} xp"
                )
            }
        }
        val progress = calculprogres(logro)
        Text(
            text = stringResource(R.string.progres) + progress + "%",
            Modifier.padding(vertical = 12.dp)
        )
        LinearProgressIndicator(
            progress = (progress / 100)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.requirment) + logro.requirement,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = stringResource(R.string.have) + logro.progress
        )
    }
}

fun calculprogres(logro: LogroProgre): Float {
    val progressPercentage = if (logro.progress > logro.requirement) {
        100.toFloat()
    } else {
        (logro.progress.toFloat() / logro.requirement.toFloat()) * 100
    }
    return String.format("%.2f", progressPercentage).replace(",", ".").toFloat()
}

@Composable
fun estrelles(logro: LogroProgre?) {
    var aux = 0

    if (logro?.type == "BRONZE") {
        aux = 0
    } else if (logro?.type == "PLATA") {
        aux = 1
    } else if (logro?.type == "OR") {
        if (logro.progress > logro.requirement) {
            aux = 3
        } else {
            aux = 2
        }
    }

    val estrellesBuides = 3 - aux
    var j = 0
    while (j < aux) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "imatgelogro",
            tint = Color.Yellow,
            modifier = Modifier.size(40.dp)
        )
        ++j
    }
    var i = 0
    while (i < estrellesBuides) {
        Icon(
            imageVector = Icons.Default.StarOutline,
            contentDescription = "imatgelogro",
            modifier = Modifier.size(40.dp)
        )
        ++i
    }
}
