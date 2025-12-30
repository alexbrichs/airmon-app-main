package com.airmon.app.compose

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.airmon.app.R
import com.airmon.app.helpers.retrieveFromDB
import com.airmon.app.ui.theme.AirmonAppTheme
import com.airmon.app.viewmodels.AirmonInfoViewModel
import com.airmon.app.viewmodels.ChatViewModel
import com.airmon.app.viewmodels.CollectionViewModel
import com.airmon.app.viewmodels.EventInfoViewModel
import com.airmon.app.viewmodels.FriendInfoViewModel
import com.airmon.app.viewmodels.FriendsViewModel
import com.airmon.app.viewmodels.InventoryViewModel
import com.airmon.app.viewmodels.ItemInfoInventoryViewModel
import com.airmon.app.viewmodels.ItemInfoShopViewModel
import com.airmon.app.viewmodels.MainChatsViewModel
import com.airmon.app.viewmodels.MapViewModel
import com.airmon.app.viewmodels.ProfileViewModel
import com.airmon.app.viewmodels.RankingViewModel
import com.airmon.app.viewmodels.RouletteViewModel
import com.airmon.app.viewmodels.SettingsViewModel
import com.airmon.app.viewmodels.ShopViewModel
import com.airmon.app.viewmodels.StationInfoViewModel
import com.airmon.app.viewmodels.TrophyInfoViewModel
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowMapScreen(mapViewModel: MapViewModel, navController: NavController) {
    val app = LocalContext.current.applicationContext as Application
    if (ContextCompat.checkSelfPermission(
        app,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    ) {
        MapScreen(mapViewModel, navController)
    }
}

@Composable
fun getNavBarItem(name: String): TabBarItem = when (name) {
    "map" -> TabBarItem(
        "map",
        stringResource(id = R.string.map),
        Icons.Filled.Map,
        Icons.Outlined.Map
    )

    "collection" -> TabBarItem(
        "collection",
        stringResource(id = R.string.collection),
        Icons.Filled.CollectionsBookmark,
        Icons.Outlined.CollectionsBookmark
    )

    "friends" -> TabBarItem(
        "friends",
        stringResource(id = R.string.friends),
        Icons.Filled.People,
        Icons.Outlined.People
    )

    "ranking" -> TabBarItem(
        "ranking",
        stringResource(id = R.string.ranking),
        Icons.Filled.BarChart,
        Icons.Outlined.BarChart
    )

    "profile" -> TabBarItem(
        "profile",
        stringResource(id = R.string.profile),
        Icons.Filled.Person,
        Icons.Outlined.Person
    )

    else -> TabBarItem("", "", Icons.Filled.Add, Icons.Outlined.Add)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Suppress("TOO_LONG_FUNCTION")
@Composable
fun MainScreen(application: Application) {
    val navController = rememberNavController()
    val mapViewModel = MapViewModel(application)
    AirmonAppTheme {
        val application = application
        val map = getNavBarItem("map")
        val collection = getNavBarItem("collection")
        val friends = getNavBarItem("friends")
        val ranking = getNavBarItem("ranking")
        val profile = getNavBarItem("profile")
        val items = listOf(map, collection, friends, ranking, profile)
        var seeBottomBar by rememberSaveable { mutableStateOf(true) }
        DisposableEffect(navController) {
            val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                val isNotChatScreen = destination.route != "ChatScreen/{parameterValue}"
                val isNotMainChatsScreen = destination.route != "MainChatsScreen"
                seeBottomBar = isNotChatScreen && isNotMainChatsScreen
            }
            navController.addOnDestinationChangedListener(listener)

            onDispose {
                navController.removeOnDestinationChangedListener(listener)
            }
        }
        var selectedTabIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    if (seeBottomBar) {
                        BottomNavView(items, navController, selectedTabIndex) { index ->
                            selectedTabIndex = index
                        }
                    }
                }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = items.first().title,
                    modifier = Modifier
                        .padding(bottom = if (seeBottomBar) 70.dp else 0.dp)
                        .fillMaxSize()
                ) {
                    composable(map.title) {
                        selectedTabIndex = 0
                        ShowMapScreen(mapViewModel = mapViewModel, navController)
                    }

                    composable(collection.title) {
                        selectedTabIndex = 1
                        CollectionScreen(
                            viewModel = CollectionViewModel(application),
                            navController = navController
                        )
                    }
                    composable(friends.title) {
                        selectedTabIndex = 2
                        FriendsScreen(
                            viewModel = FriendsViewModel(application),
                            navController = navController
                        )
                    }
                    composable(ranking.title) {
                        selectedTabIndex = 3
                        RankingScreen(
                            viewModel = RankingViewModel(application),
                            navController = navController
                        )
                    }
                    composable(profile.title) {
                        selectedTabIndex = 4
                        val username: String?
                        runBlocking {
                            username = retrieveFromDB(application, "username")
                        }
                        ProfileScreen(
                            viewModel = ProfileViewModel(application, requireNotNull(username)),
                            navController = navController,
                            LocalContext.current.applicationContext
                        )
                    }
                    composable("AirmonInfo/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue")).toIntOrNull()
                        AirmonInfoScreen(
                            AirmonInfoViewModel(application, requireNotNull(parameterValue)),
                            navController
                        )
                    }
                    composable("FriendInfo/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        FriendInfoScreen(
                            FriendInfoViewModel(application, parameterValue), navController,
                            LocalContext.current.applicationContext
                        )
                    }
                    composable("StationInfo/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        StationInfoScreen(StationInfoViewModel(parameterValue), navController)
                    }
                    composable("MainChatsScreen") {
                        MainChatsScreen(
                            MainChatsViewModel(application),
                            navController,
                            friends.title
                        )
                    }
                    composable("ChatScreen/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        val token: String?
                        val username: String?
                        runBlocking {
                            token = retrieveFromDB(application, "token")
                            username = retrieveFromDB(application, "username")
                        }

                        ChatScreen(ChatViewModel(application, parameterValue, token, username), navController)
                    }
                    composable("ShopScreen") {
                        ShopScreen(ShopViewModel(application), navController, profile.title)
                    }
                    composable(
                        "Roulette?checkSpinned={checkSpinned}",
                        arguments = listOf(navArgument("checkSpinned") { defaultValue = false })
                    ) {from ->
                        val checkSpinned =
                            requireNotNull(from.arguments?.getBoolean("checkSpinned"))
                        RouletteScreen(RouletteViewModel(application, checkSpinned = checkSpinned), navController)
                    }
                    composable("SettingsScreen") {
                        SettingsScreen(
                            SettingsViewModel(application), navController,
                            LocalContext.current.applicationContext
                        )
                    }
                    composable("InventoryScreen") {
                        InventoryScreen(
                            InventoryViewModel(application),
                            navController,
                            profile.title
                        )
                    }
                    composable("TrophyInfoScreen/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        TrophyInfoScreen(
                            TrophyInfoViewModel(application, parameterValue), navController,
                            LocalContext.current.applicationContext
                        )
                    }
                    composable("ItemShop/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        ItemInfoShopScreen(ItemInfoShopViewModel(parameterValue), navController)
                    }
                    composable("ItemInventory/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        ItemInfoInventoryScreen(
                            ItemInfoInventoryViewModel(
                                application,
                                parameterValue
                            ), navController
                        )
                    }
                    composable("RegressiveScreen") {
                        CountdownScreen(navController, application)
                    }
                    composable("AirboxInfo/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue")).toIntOrNull()
                        AirboxInfoScreen(
                            AirmonInfoViewModel(application, requireNotNull(parameterValue)),
                            navController
                        )
                    }
                    composable("EventInfo/{parameterValue}") { backStackEntry ->
                        val parameterValue =
                            requireNotNull(backStackEntry.arguments?.getString("parameterValue"))
                        EventInfoScreen(EventInfoViewModel(parameterValue))
                    }
                }
            }
        }
    }
}
