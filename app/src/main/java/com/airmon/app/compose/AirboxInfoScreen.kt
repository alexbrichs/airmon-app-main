package com.airmon.app.compose

import android.app.Application
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.AirmonInfoViewModel

@Composable
fun AirboxInfoScreen(viewModel: AirmonInfoViewModel, navController: NavController) {
    val name = viewModel.name
    val date = viewModel.date
    val rarity = viewModel.rarity
    val type = viewModel.type

    NavigationsInventory(navController)

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
                text = stringResource(R.string.airmonInfo),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        AirmonName(name)
        AirmonImage(name)
        AirmonType(type)
        AirmonRarity(rarity)
        AirmonDate(date)
        AirmonDescription(name)
    }
}

@Composable
fun NavigationsInventory(navController: NavController) {
    val context = LocalContext.current

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun handleOnBackPressed() {
            navController.navigate("InventoryScreen")
        }
    }

    LocalOnBackPressedDispatcherOwner.current
        ?.onBackPressedDispatcher
        ?.addCallback(onBackPressedCallback)
}

@Preview
@Composable
private fun previewPreview() {
    AirboxInfoScreen(
        viewModel = AirmonInfoViewModel(Application(), 3),
        navController = NavController(Application())
    )
}

@Composable
private fun AirmonDescription(name: String) {
    val nameId = LocalContext.current.resources.getIdentifier(
        name,
        "string",
        LocalContext.current.packageName
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = stringResource(nameId),
            style = InfoTypography.displayMedium
        )
    }
}

@Composable
private fun AirmonDate(date: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = stringResource(R.string.date) + ": " + date,
            style = InfoTypography.displayMedium
        )
    }
}

@Composable
private fun AirmonName(name: String) {
    Text(
        text = name,
        style = InfoTypography.titleLarge,
        modifier = Modifier.padding(vertical = 54.dp)
    )
}

@Composable
private fun AirmonImage(name: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
        val context = LocalContext.current
        if (name != "") {
            val imageId = context.resources.getIdentifier(
                name.lowercase(),
                "raw",
                context.packageName
            )
            val painter = painterResource(imageId)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
