package com.airmon.app.compose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.ItemInfoInventoryViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemInfoInventoryScreen(viewModel: ItemInfoInventoryViewModel, navController: NavController) {
    val name = viewModel.name
    val description = viewModel.description
    val image = viewModel.image
    val duration = viewModel.duration
    val price = viewModel.price.toString()
    val context = LocalContext.current

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
            IconButton(onClick = { navController.navigate("InventoryScreen") }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
            }

            Text(
                text = stringResource(R.string.itemInformation),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        ItemName(name)
        ItemImage(image)
        ItemPrice(price, context)
        ItemDuration(duration, context)
        ItemDescription(description)
        ItemUseButton(viewModel, navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ItemUseButton(
    viewModel: ItemInfoInventoryViewModel,
    navController: NavController,
) {
    val quantity by viewModel.quantity.collectAsState()
    val showErrorDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.padding(10.dp),
        onClick = {
            if (quantity > 0) {
                if (viewModel.name == "airbox") {
                    navController.navigate("RegressiveScreen")
                } else if (viewModel.name == "extra_roulette_spin") {
                    viewModel.addActiveItem()
                    val checkSpinned = false
                    navController.navigate("Roulette?checkSpinned=$checkSpinned")
                } else {
                    viewModel.addActiveItem()
                    showSuccessDialog.value = true
                }
            } else {
                viewModel.removeItem()
                showErrorDialog.value = true
            }
        },
        content = {
            Text(stringResource(R.string.use))
        }
    )

    if (showSuccessDialog.value) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog.value = false },
            title = { Text(stringResource(R.string.itemActivated)) },
            confirmButton = {
                Button(onClick = { showSuccessDialog.value = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    } else if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = { Text(stringResource(R.string.errorNoItems)) },
            text = { Text(stringResource(R.string.extendedNoItems)) },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog.value = false
                    navController.navigate("InventoryScreen")
                }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
private fun ItemDescription(description: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = description,
            style = InfoTypography.displayMedium
        )
    }
}

@Composable
private fun ItemPrice(price: String, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val coinsString = context.getString(R.string.coinsprice).lowercase()
        val priceString = context.getString(R.string.price)
        Text(
            text = "$priceString: $price $coinsString ",
            style = InfoTypography.displayMedium
        )
    }
}

@Composable
private fun ItemDuration(duration: String, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (duration == "00:00:00") {
            Text(
                text = context.getString(R.string.single_use),
                style = InfoTypography.displayMedium
            )
        } else {
            val durationString = context.getString(R.string.duration)
            Text(
                text = "$durationString: $duration",
                style = InfoTypography.displayMedium
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun ItemName(name: String) {
    val stringId = LocalContext.current.resources.getIdentifier(
        name,
        "string",
        LocalContext.current.packageName
    )
    Text(
        text = stringResource(stringId),
        style = InfoTypography.titleLarge,
        modifier = Modifier.padding(vertical = 64.dp)
    )
}

@Composable
private fun ItemImage(image: String) {
    Box(contentAlignment = Alignment.Center) {
        val painter: Painter = rememberImagePainter(
            data = image,
            builder = {

                }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}
