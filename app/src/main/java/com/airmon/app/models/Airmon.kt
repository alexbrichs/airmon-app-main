package com.airmon.app.models

import androidx.compose.ui.graphics.Color
import com.airmon.app.R

/**
 * @property name
 * @property description
 * @property rarity
 * @property type
 * @property image
 */
data class Airmon(
    val name: String,
    val description: String,
    val rarity: AirmonRarity,
    val type: AirmonType,
    val image: String
)

/**
 * @property backgroundColor
 * @property textColor
 */
enum class AirmonType(val backgroundColor: Long, val textColor: Color) {
    C6H6(0xFF_FF1_493, Color.Black),
    CO(0xFF_808_080, Color.White),
    H2S(0xFF_90E_E90, Color.Black),
    NO2(0xFF_8B4_513, Color.White),
    O3(0xFF_483_D8B, Color.White),
    PM10(0xFF_FFA_500, Color.Black),
    PM25(0xFF_696_969, Color.White),
    SO2(0xFF_FFF_F00, Color.Black),
    UNKNOWN(0xFF_FFF_FFF, Color.Black),
    ;

    fun translation(): String = when (this) {
        UNKNOWN -> "???"
        else -> this.name
    }
}

/**
 * @property backGroundColor
 * @property textColor
 */
enum class AirmonRarity(val backGroundColor: Long, val textColor: Color) {
    COMMON(0xFF_14D_63D, Color.Black),
    EPIC(0xFF_A41_2FF, Color.White),
    LEGENDARY(0xFF_D4A_F37, Color.Black),
    MYTHICAL(0xFF_FF1_212, Color.Black),
    SPECIAL(0xFF_127_DFF, Color.White),
    UNKNOWN(0xFF_FFF_FFF, Color.Black),
    ;

    fun translationKey(): Int = when (this) {
        LEGENDARY -> R.string.legendary
        MYTHICAL -> R.string.mythical
        EPIC -> R.string.epic
        SPECIAL -> R.string.special
        COMMON -> R.string.common
        UNKNOWN -> R.string.unknown
    }
}
