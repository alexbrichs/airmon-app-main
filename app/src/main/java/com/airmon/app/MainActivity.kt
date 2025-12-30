package com.airmon.app

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airmon.app.compose.MainScreen
import com.airmon.app.models.AirmonRegistry
import com.airmon.app.models.EventRegistry
import com.airmon.app.models.FriendUserRegistry
import com.airmon.app.models.ItemActiveRegistry
import com.airmon.app.models.StationRegistry
import com.airmon.app.ui.theme.AirmonAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AirmonAppTheme {
                AirmonRegistry.getAirmons(application)
                StationRegistry.getStations(application)
                EventRegistry.getEvents(application)
                FriendUserRegistry.getFriends(application)
                ItemActiveRegistry.getItemsActive(application)

                MainScreen(application = application)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, locale = "ca")
@Composable
private fun MainPreview() {
    AirmonAppTheme {
        MainScreen(application = Application())
    }
}
