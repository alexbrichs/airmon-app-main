package com.airmon.app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.ui.theme.CustomTypography
import com.airmon.app.viewmodels.CollectionViewModel

@Composable
fun CollectionScreen(viewModel: CollectionViewModel, navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.collection),
            style = CustomTypography.titleSmall
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), contentAlignment = Alignment.TopCenter
        ) {
            Collection(viewModel, navController)
        }
    }
}

@Composable
fun Collection(viewModel: CollectionViewModel, navController: NavController) {
    val airmonNames: List<Pair<Int, String>> by viewModel.listAirmons.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(airmonNames) { airmonPair ->
            Box(modifier = Modifier
                .clickable {
                    navController.navigate("AirmonInfo/${airmonPair.first}")
                }
                .fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val context = LocalContext.current
                        if (airmonPair.second != "") {
                            val imageId = context.resources.getIdentifier(
                                airmonPair.second.lowercase(),
                                "raw",
                                context.packageName
                            )
                            val painter = painterResource(imageId)
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                    Text(text = airmonPair.second)
                }
            }
        }
    }
}
