package com.airmon.app.compose

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.models.Estadistiques
import com.airmon.app.models.Logro
import com.airmon.app.ui.theme.CustomTypography
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.ProfileViewModel

@Composable
fun RowScope.TableCellIcon(
    icon: ImageVector,
    weight: Float,
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.weight(weight)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
    context: Context
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("ShopScreen")
                    }
                ) {
                    TableCellIcon(Icons.Filled.ShoppingBag, 0.3f)
                    TableCell(
                        text = stringResource(R.string.itemsStore),
                        weight = 0.7f,
                        fontStyle = CustomTypography.displaySmall,
                        textAlign = TextAlign.Left
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate("InventoryScreen")
                    }
                ) {
                    TableCellIcon(Icons.Filled.Backpack, 0.3f)
                    TableCell(
                        text = stringResource(R.string.inventory),
                        weight = 0.7f,
                        fontStyle = CustomTypography.displaySmall,
                        textAlign = TextAlign.Left
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val checkSpinned = true
                        navController.navigate("Roulette?checkSpinned=$checkSpinned")
                    }
                ) {
                    TableCellIcon(Icons.Filled.Casino, 0.3f)
                    TableCell(
                        text = stringResource(R.string.roulette),
                        weight = 0.7f,
                        fontStyle = CustomTypography.displaySmall,
                        textAlign = TextAlign.Left
                    )
                }

                ImagePickerRow(viewModel)
                Spacer(modifier = Modifier.height(32.dp))
            }
        },
        sheetPeekHeight = 38.dp,
        sheetShape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxSize()
    ) {
        DisplayProfile(viewModel, navController, context)
    }
}

@Composable
fun ImagePickerRow(
    viewModel: ProfileViewModel
) {
    val photoLaucher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    viewModel.uploadImage(uri)
                }
            })
    Row(modifier = Modifier.clickable {
        photoLaucher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }, verticalAlignment = Alignment.CenterVertically) {
        TableCellIcon(Icons.Filled.PhotoCamera, 0.3f)
        TableCell(
            text = stringResource(R.string.postImage),
            weight = 0.7f,
            fontStyle = CustomTypography.displaySmall,
            textAlign = TextAlign.Left
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayProfile(
    viewModel: ProfileViewModel,
    navController: NavController,
    context: Context
) {
    val name = viewModel.name

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

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.profile)) },
            actions = {
                IconButton(onClick = { navController.navigate("MainChatsScreen") }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        Modifier
                            .clickable {
                                navController.navigate("SettingsScreen")
                            })
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            avatarUsuari(viewModel, name)

            Text(
                text = name,
                style = InfoTypography.titleMedium,
            )
            val xp = viewModel.obteExp(name)
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
            var showStatistics by remember { mutableStateOf(true) }
            var showLogros by remember { mutableStateOf(false) }
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
                mostraLogrosPersonals(llistaLogros, context, navController)
            } else if (showStatistics) {
                mostraEStadistiques(llistaEstadistiques, estadistiques)
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .padding(bottom = 30.dp),
                onClick = { viewModel.onLogoutPressed() },
                content = {
                    Text(stringResource(R.string.logoutVerb))
                }
            )
        }
    }
}

@Composable
fun mostraLogrosPersonals(
    llisaLogros: List<Logro>?,
    context: Context,
    navController: NavController
) {
    Column {
        llisaLogros?.forEach { logro ->
            Row(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .clickable {
                        navController.navigate("TrophyInfoScreen/${logro.name}")
                    }
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

fun getStringByName(context: Context, resourceName: String): String {
    val resId = context.resources.getIdentifier(resourceName, "string", context.packageName)
    return if (resId != 0) {
        context.getString(resId)
    } else {
        "Resource not found"
    }
}

@Composable
fun mostraEStadistiques(llistaEstadistiques: List<String>, estadistiques: Estadistiques?) {
    var est = 0
    Column {
        llistaEstadistiques.forEach { estadistica ->
            Row {
                var num = obteNum(est, estadistiques)
                Text(
                    text = "$estadistica $num",
                    style = InfoTypography.titleMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                ++est
            }
        }
    }
}

fun obteNum(est: Int, estadistiques: Estadistiques?): Int? {
    val num: Int?
    if (est == 0) {
        num = estadistiques?.n_airmons_capturats
    } else if (est == 1) {
        num = estadistiques?.airmons_alliberats
    } else if (est == 2) {
        num = estadistiques?.n_consumibles_usats
    } else if (est == 3) {
        num = estadistiques?.n_tirades_ruleta
    } else if (est == 4) {
        num = estadistiques?.total_coins
    } else if (est == 5) {
        num = estadistiques?.total_airmons_common
    } else if (est == 6) {
        num = estadistiques?.total_airmons_special
    } else if (est == 7) {
        num = estadistiques?.total_airmons_epic
    } else if (est == 8) {
        num = estadistiques?.total_airmons_mythical
    } else if (est == 9) {
        num = estadistiques?.total_airmons_legendary
    } else {
        num = estadistiques?.total_compres
    }
    return num
}

fun ordenaLogros(logros: List<Logro>?): List<Logro> {
    val millorsLogros: MutableMap<String, Logro> = mutableMapOf()

    logros?.let {
        for (logro in logros) {
            val nom = logro.name
            val logroExistente = millorsLogros[nom]
            if (logroExistente == null) {
                millorsLogros[nom] = logro
            } else if (logro.data == null && logroExistente.data == null &&
                    isBetterType(logroExistente.type, logro.type)
            ) {
                millorsLogros[nom] = logro
            } else if (logro.data != null && logroExistente.data == null) {
                millorsLogros[nom] = logro
            } else if (logro.data != null && isBetterType(logro.type, logroExistente.type)) {
                millorsLogros[nom] = logro
            }
        }
    }

    return millorsLogros.values.toList()
}

fun isBetterType(tipusActual: String, tipusExistent: String): Boolean {
    val prioridad = mapOf("BRONZE" to 0, "PLATA" to 1, "OR" to 2)
    return prioridad.getValue(tipusActual) > prioridad.getValue(tipusExistent)
}

@Composable
fun avatarUsuari(viewModel: ProfileViewModel, nom: String) {
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
