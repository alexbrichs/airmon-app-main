package com.airmon.app.compose

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.models.Logro
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.FriendInfoViewModel
import kotlin.math.sqrt

@Composable
fun FriendInfoScreen(
    viewModel: FriendInfoViewModel,
    navController: NavController,
    context: Context
) {
    val name = viewModel.name
    val xp = viewModel.obteExp(name)
    val estadistiques = viewModel.getEstadistiques(name)
    val llistaEstadistiques: List<String> = listOf(
        stringResource(R.string.total_n_captures),
        stringResource(R.string.total_n_released),
        stringResource(R.string.total_n_consumables_used),
        stringResource(R.string.total_n_roulette_spins),
        stringResource(R.string.total_n_monedes),
        stringResource(R.string.total_n_airmon_common),
        stringResource(R.string.total_n_airmon_special),
        stringResource(R.string.total_n_airmon_epic),
        stringResource(R.string.total_n_airmon_mythical),
        stringResource(R.string.total_n_airmon_legendary),
        stringResource(R.string.total_n_purchase)
    )

    val logros = viewModel.getLogros(name)
    val llistaLogros = ordenaLogros(logros)

    var showStatistics by remember { mutableStateOf(true) }
    var showLogros by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
            }

            Text(
                text = stringResource(R.string.profile),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        avatarUsuari(viewModel, name)

        Text(
            text = name,
            style = InfoTypography.titleMedium,
        )
        val nivell = calculaNivell(xp)
        Text(
            text = stringResource(R.string.level) + nivell,
            style = InfoTypography.titleMedium,
        )
        val percentatge = calculaPercentatge(xp)
        LinearProgressIndicator(
            progress = percentatge,
            Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.statistics),
                style = InfoTypography.titleMedium,
                modifier = Modifier
                    .clickable {
                        if (showLogros) {
                            showLogros = false
                        }
                        showStatistics = true
                    }
            )
            Spacer(modifier = Modifier.width(50.dp))
            Icon(
                imageVector = Icons.Default.HorizontalRule,
                contentDescription = "separacio"
            )
            Spacer(modifier = Modifier.width(50.dp))
            Text(
                text = stringResource(R.string.achievements),
                style = InfoTypography.titleMedium,
                modifier = Modifier
                    .clickable {
                        if (showStatistics) {
                            showStatistics = false
                        }
                        showLogros = true
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (showLogros) {
            mostraLogrosAmic(llistaLogros, context, navController)
        } else if (showStatistics) {
            mostraEStadistiques(llistaEstadistiques, estadistiques)
        }
    }
}

fun calculaNivell(xp: Int?): Int {
    val factr_exp = 0.1
    return (factr_exp * sqrt(xp!!.toDouble())).toInt()
}

fun calculaPercentatge(xp: Int?): Float {
    val factr_exp = 0.1
    val nivell = (factr_exp * sqrt(xp!!.toDouble())).toFloat()
    return ((nivell * 100) % 100) / 100
}

@Composable
fun avatarUsuari(viewModel: FriendInfoViewModel, nom: String) {
    val avatar = viewModel.getAvatar(nom)
    avatar?.let {
        carregaAvatarGran(avatar)
    } ?: Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Imatge usuari",
        Modifier
            .size(75.dp)
    )
}

@Composable
fun carregaAvatarGran(avatar: String) {
    val painter = rememberImagePainter(
        data = avatar,
        builder = {}
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(90.dp)
    )
}

@Composable
fun mostraLogrosAmic(
    llisaLogros: List<Logro>?,
    context: Context,
    navController: NavController
) {
    Column {
        llisaLogros?.forEach { logro ->
            Row(
                modifier = Modifier
                    .padding(bottom = 7.dp)
            ) {
                var aux = 0
                logro.data?.let {
                    if (logro.type == "BRONZE") {
                        aux = 1
                    } else if (logro.type == "PLATA") {
                        aux = 2
                    } else if (logro.type == "OR") {
                        aux = 3
                    }
                }

                val estrellesBuides = 3 - aux
                var j = 0
                while (j < aux) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "imatgelogro",
                        tint = Color.Yellow
                    )
                    ++j
                }
                var i = 0
                while (i < estrellesBuides) {
                    Icon(
                        imageVector = Icons.Default.StarOutline,
                        contentDescription = "imatgelogro"
                    )
                    ++i
                }
                Text(
                    text = getStringByName(context, logro.name),
                    style = InfoTypography.titleMedium
                )
            }
        }
    }
}
