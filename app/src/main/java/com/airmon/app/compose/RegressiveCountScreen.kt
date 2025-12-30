package com.airmon.app.compose

import android.app.Application
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airmon.app.models.AirmonCollectionRegistry
import kotlinx.coroutines.delay

@Composable
fun CountdownScreen(navController: NavController, application: Application) {
    var count by remember { mutableIntStateOf(3) }

    LaunchedEffect(key1 = count) {
        if (count > 0) {
            delay(1000L)
            count--
        } else {
            val airmonId = AirmonCollectionRegistry.airboxItem(application)
            navController.navigate("AirboxInfo/$airmonId")
        }
    }
    var animate by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (animate) 1.5f else 1f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    LaunchedEffect(key1 = count) {
        animate = !animate
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = if (count > 0) count.toString() else "1",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.scale(scale)
        )
    }
}
