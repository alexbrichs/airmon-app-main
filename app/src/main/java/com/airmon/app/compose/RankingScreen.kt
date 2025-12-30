package com.airmon.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airmon.app.R
import com.airmon.app.ui.theme.CustomTypography
import com.airmon.app.viewmodels.RankingViewModel

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    fontStyle: TextStyle,
    textAlign: TextAlign
) {
    Text(
        text = text,
        style = fontStyle,
        modifier = Modifier
            .border(1.dp, Color.Transparent)
            .weight(weight)
            .padding(8.dp),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        textAlign = textAlign
    )
}

@Composable
fun RankingUsers(
    viewModel: RankingViewModel,
    navController: NavController,
    lightBlue: Color
) {
    val statistics = listOf(
        stringResource(R.string.total_n_captures).removeSuffix(":"),
        stringResource(R.string.total_n_released).removeSuffix(":"),
        stringResource(R.string.total_n_consumables_used).removeSuffix(":"),
        stringResource(R.string.total_n_roulette_spins).removeSuffix(":"),
        stringResource(R.string.total_n_monedes).removeSuffix(":"),
        stringResource(R.string.total_n_airmon_common).removeSuffix(":"),
        stringResource(R.string.total_n_airmon_special).removeSuffix(":"),
        stringResource(R.string.total_n_airmon_epic).removeSuffix(":"),
        stringResource(R.string.total_n_airmon_mythical).removeSuffix(":"),
        stringResource(R.string.total_n_airmon_legendary).removeSuffix(":"),
        stringResource(R.string.total_n_purchase).removeSuffix(":"),
        stringResource(R.string.xp)
    )
    var users by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var selectedStatistic by remember { mutableStateOf(statistics[0]) }

    LaunchedEffect(selectedStatistic) {
        val apiStatistic = viewModel.getStatistic(statistics, selectedStatistic)
        val newUsers = viewModel.getUsersByStatistic(apiStatistic)
        users = newUsers
    }
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        OutlinedButton(modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true }) {
            Text(text = selectedStatistic, fontSize = 24.sp)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            statistics.forEach { statistic ->
                DropdownMenuItem({
                    Text(
                        text = statistic,
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }, onClick = {
                    selectedStatistic = statistic
                    expanded = false
                })
            }
        }
    }

    val column1Weight = .5f  // 50%
    val column2Weight = .5f  // 50%

    Row(Modifier.padding(top = 16.dp, bottom = 16.dp)) {
        TableCell(
            text = "  ${stringResource(R.string.position)}",
            weight = column1Weight,
            fontStyle = CustomTypography.displayMedium,
            textAlign = TextAlign.Center
        )
        TableCell(
            text = stringResource(R.string.user),
            weight = column2Weight,
            fontStyle = CustomTypography.displayMedium,
            textAlign = TextAlign.Center

        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(lightBlue)
    )
    users.forEachIndexed() { index, user ->
        RowPrintUser(user, index, column1Weight, column2Weight, navController)
    }
}

@Composable
fun RankingZones(
    viewModel: RankingViewModel,
    navController: NavController,
    searchText: MutableState<String>,
    lightBlue: Color
) {
    SearchZoneBar(searchText)
    val zones = viewModel.getStations(searchText.value)
    val column1Weight = .5f  // 50%
    val column2Weight = .5f  // 50%
    Row(Modifier.padding(top = 16.dp, bottom = 16.dp)) {
        TableCell(
            text = "  ${stringResource(R.string.position)}",
            weight = column1Weight,
            fontStyle = CustomTypography.displayMedium,
            textAlign = TextAlign.Center
        )
        TableCell(
            text = stringResource(R.string.zone),
            weight = column2Weight,
            fontStyle = CustomTypography.displayMedium,
            textAlign = TextAlign.Center

        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(lightBlue)
    )
    zones.forEachIndexed() { index, zone ->
        RowPrintZone(zone, index, column1Weight, column2Weight, navController)
    }
}

@Composable
fun RankingScreen(viewModel: RankingViewModel, navController: NavController) {
    val lightBlue = Color(0xff_42a_af5)
    val searchText: MutableState<String> = remember { mutableStateOf("") }
    var selectedButton by remember { mutableStateOf("zones") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.ranking),
                style = CustomTypography.titleSmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.zones).uppercase(),
                    modifier = Modifier.clickable { selectedButton = "zones" },
                    color = if (selectedButton == "zones") lightBlue else Color.Black,
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
                    text = stringResource(R.string.users).uppercase(),
                    modifier = Modifier.clickable { selectedButton = "users" },
                    color = if (selectedButton == "users") lightBlue else Color.Black,
                    textAlign = TextAlign.Center,
                    style = CustomTypography.titleSmall,
                    fontSize = 28.sp
                )
            }
        }
        if (selectedButton == "zones") {
            item {
                RankingZones(viewModel, navController, searchText, lightBlue)
            }
        } else if (selectedButton == "users") {
            item {
                RankingUsers(viewModel, navController, lightBlue)
            }
        }
    }
}

@Composable
fun RowPrintZone(
    zone: String,
    index: Int,
    column1Weight: Float,
    column2Weight: Float,
    navController: NavController
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 16.dp)
        .clickable { navController.navigate("StationInfo/$zone") }) {
        TableCell(
            text = "   ${index + 1}",
            weight = column1Weight,
            fontStyle = CustomTypography.displaySmall,
            textAlign = TextAlign.Center

        )
        TableCell(
            text = zone,
            weight = column2Weight,
            fontStyle = CustomTypography.displaySmall,
            textAlign = TextAlign.Center

        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.LightGray)
    )
}

@Composable
fun RowPrintUser(
    user: Pair<String, Int>,
    index: Int,
    column1Weight: Float,
    column2Weight: Float,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .clickable { navController.navigate("FriendInfo/${user.first}") }
    ) {
        TableCell(
            text = "   ${index + 1}(${user.second})",
            weight = column1Weight,
            fontStyle = CustomTypography.displaySmall,
            textAlign = TextAlign.Center

        )
        TableCell(
            text = user.first,
            weight = column2Weight,
            fontStyle = CustomTypography.displaySmall,
            textAlign = TextAlign.Center

        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.LightGray)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchZoneBar(searchText: MutableState<String>) {
    TextField(
        value = searchText.value,
        onValueChange = { searchText.value = it },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        placeholder = {
            Text(
                stringResource(R.string.search),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        },
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}
