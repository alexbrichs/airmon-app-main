package com.airmon.app.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter
import com.airmon.app.R
import com.airmon.app.viewmodels.ShopViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel,
    navController: NavController,
    profileScreen: String
) {
    val coins by viewModel.coins.collectAsState()
    val showNotEnoughCoinsDialog = remember { mutableStateOf(false) }
    val showPurchaseSuccessfulDialog = remember { mutableStateOf(false) }

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
                text = stringResource(R.string.itemsStore),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Text(
                text = stringResource(R.string.coins, coins),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), contentAlignment = Alignment.TopCenter
        ) {
            Shop(
                viewModel,
                navController,
                coins,
                showNotEnoughCoinsDialog,
                showPurchaseSuccessfulDialog
            )
        }
    }

    if (showNotEnoughCoinsDialog.value) {
        AlertDialog(
            onDismissRequest = { showNotEnoughCoinsDialog.value = false },
            title = { Text(stringResource(R.string.notEnoughCoins)) },
            text = { Text(stringResource(R.string.notEnoughCoinsText)) },
            confirmButton = {
                Button(onClick = { showNotEnoughCoinsDialog.value = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    if (showPurchaseSuccessfulDialog.value) {
        AlertDialog(
            onDismissRequest = { showPurchaseSuccessfulDialog.value = false },
            title = { Text(stringResource(R.string.successfulPurchase)) },
            text = { Text(stringResource(R.string.successfulPurchaseText)) },
            confirmButton = {
                Button(onClick = { showPurchaseSuccessfulDialog.value = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun Shop(
    viewModel: ShopViewModel,
    navController: NavController,
    coins: Int,
    showNotEnoughCoinsDialog: MutableState<Boolean>,
    showPurchaseSuccessfulDialog: MutableState<Boolean>
) {
    val consumiblesNames = viewModel.getListItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(consumiblesNames.size * 3) { index ->
            val name = consumiblesNames[index / 3]
            val ncons = when (index % 3) {
                0 -> 1
                1 -> 3
                else -> 9
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.getItem(name)?.let { item ->
                    val painter = rememberAsyncImagePainter(model = item.image)
                    Box(modifier = Modifier
                        .clickable {
                            navController.navigate("ItemShop/$name")
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

                    Text(
                        text = "x$ncons",
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

                    val stringId = LocalContext.current.resources.getIdentifier(
                        name,
                        "string",
                        LocalContext.current.packageName
                    )
                    Text(
                        text = stringResource(stringId),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(4.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )

                    val preu = item.price * ncons
                    Text(
                        text = preu.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(4.dp)
                    )

                    Button(onClick = {
                        if (preu > coins) {
                            showNotEnoughCoinsDialog.value = true
                        } else {
                            viewModel.buyItem(item, ncons)
                            showPurchaseSuccessfulDialog.value = true
                        }
                    }) {
                        Text(text = stringResource(R.string.buy).uppercase())
                    }
                }
            }
        }
    }
}
