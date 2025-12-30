package com.airmon.app.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Stars
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @property quantity
 * @property type
 */
data class RoulettePrize(
    val type: PrizeType,
    val quantity: Int
)

/**
 * @property icon
 */
enum class PrizeType(val icon: ImageVector) {
    Airbox(Icons.Filled.CardGiftcard),
    CoinBooster(Icons.Filled.ArrowCircleUp),
    Coins(Icons.Filled.AttachMoney),
    SpinAgain(Icons.Filled.Replay),
    XP(Icons.Filled.Stars),
    XPBooster(Icons.Filled.RocketLaunch),
    ;
}
