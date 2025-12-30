package com.airmon.app.compose

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter
import com.airmon.app.R
import com.airmon.app.ui.theme.CustomTypography
import com.airmon.app.viewmodels.InventoryViewModel

import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel,
    navController: NavController,
    profileScreen: String
) {
    val lightBlue = Color(0xff_42a_af5)
    ResetBackButtonInv(navController)

    Column(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate(profileScreen) }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
            }

            Text(
                text = stringResource(R.string.inventory),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        var inventory by remember { mutableStateOf(true) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.allItems).uppercase(),
                modifier = Modifier.clickable { inventory = true },
                color = if (inventory) lightBlue else Color.Black,
                textAlign = TextAlign.Center,
                style = CustomTypography.titleSmall,
                fontSize = 28.sp
            )
            Text(
                text = " | ",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = CustomTypography.titleSmall,
                fontSize = 44.sp
            )
            Text(
                text = stringResource(R.string.activeItems).uppercase(),
                modifier = Modifier.clickable { inventory = false },
                color = if (!inventory) lightBlue else Color.Black,
                textAlign = TextAlign.Center,
                style = CustomTypography.titleSmall,
                fontSize = 28.sp
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        if (inventory) {
            ItemsInventory(viewModel, navController)
        } else {
            ItemsActive(viewModel, navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemsActive(viewModel: InventoryViewModel, navController: NavController) {
    val activeItems by viewModel.activeItems.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(activeItems) { item ->
            ItemActiveContainer(item, viewModel, navController)
        }
    }
}

@Composable
fun ItemsInventory(viewModel: InventoryViewModel, navController: NavController) {
    val consumiblesNames = viewModel.getInventoryItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(consumiblesNames) { item ->
            ItemContainer(item, viewModel, navController)
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ItemContainer(
    item: String,
    viewModel: InventoryViewModel,
    navController: NavController
) {
    val inventoryItem = viewModel.getItemInventory(item)
    if (inventoryItem != null && inventoryItem.quantity == 0) {
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                navController.navigate("ItemInventory/$item")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrintImage(navController, item, viewModel)

        val qtt = inventoryItem?.quantity ?: 0
        Text(
            text = "x$qtt",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = (-10).dp)
                .border(
                    3.dp, Color.Black, RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
                .background(Color.White)
        )

        ItemDisplayName(item)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun ItemActiveContainer(
    item: String,
    viewModel: InventoryViewModel,
    navController: NavController
) {
    val activeItem = viewModel.getItemActive(item)
    var timeLeft by remember { mutableStateOf("") }

    LaunchedEffect(key1 = item) {
        while (true) {
            val expiration = activeItem?.expiration ?: ""
            val expirationDate = ZonedDateTime.parse(expiration)
            val duration = calculateTimeLeft(expirationDate)
            if (duration.isNegative) {
                viewModel.removeActiveItem(item)
                break
            } else {
                timeLeft = formatDuration(duration)
                delay(1000L)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrintImage(navController, item, viewModel)

        Text(
            text = timeLeft.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = (-10).dp)
                .border(
                    3.dp, Color.Black, RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
                .background(Color.White)
        )

        ItemDisplayName(item)
    }
}

@Composable
fun PrintImage(
    navController: NavController,
    item: String,
    viewModel: InventoryViewModel
) {
    val itemImage = viewModel.getItemImage(item)
    val painter = rememberAsyncImagePainter(model = itemImage)

    Box(modifier = Modifier
        .clickable {
            navController.navigate("ItemInventory/$item")
        }
        .size(80.dp)
        .clip(CircleShape)
        .background(Color.LightGray)) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ItemDisplayName(item: String) {
    val stringId = LocalContext.current.resources.getIdentifier(
        item,
        "string",
        LocalContext.current.packageName
    )
    Text(
        text = stringResource(stringId),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
fun ResetBackButtonInv(navController: NavController) {
    val originalOnBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(originalOnBackPressedCallback)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateTimeLeft(expiration: ZonedDateTime): Duration {
    val now = ZonedDateTime.now(ZoneId.of("UTC+2"))
    return Duration.between(now, expiration)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}
