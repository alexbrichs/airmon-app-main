package com.airmon.app.compose

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.ItemInfoShopViewModel

@Composable
fun ItemInfoShopScreen(viewModel: ItemInfoShopViewModel, navController: NavController) {
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
        ItemName(name)
        ItemImage(image)
        ItemPrice(price, context)
        ItemDuration(duration, context)
        ItemDescription(description)
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
