package com.airmon.app.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.airmon.app.R
import com.airmon.app.api.ProfileApiInterfaceImplementation
import com.airmon.app.models.PrizeType.Airbox
import com.airmon.app.models.PrizeType.CoinBooster
import com.airmon.app.models.PrizeType.Coins
import com.airmon.app.models.PrizeType.SpinAgain
import com.airmon.app.models.PrizeType.XP
import com.airmon.app.models.PrizeType.XPBooster
import com.airmon.app.models.RoulettePrize

import java.time.LocalDate

import kotlinx.coroutines.runBlocking

/**
 * @property application
 * @property checkSpinned
 */
class RouletteViewModel(val application: Application, val checkSpinned: Boolean) : ViewModel() {
    private val _canSpin: MutableLiveData<Boolean> = MutableLiveData()
    val canSpin: LiveData<Boolean> = _canSpin

    init {
        runBlocking {
            if (!checkSpinned) {
                _canSpin.value = true
                return@runBlocking
            }
            val lastSpin: String = ProfileApiInterfaceImplementation.getLastSpin(application)
            if (lastSpin == "") {
                _canSpin.value = true
            } else {
                val lastSpinFormatted = LocalDate.parse(lastSpin)
                _canSpin.value = LocalDate.now() > lastSpinFormatted
            }
        }
    }

    fun availablePrizes(): List<RoulettePrize> {
        val prizes: List<RoulettePrize> = listOf(
            RoulettePrize(Coins, 10),
            RoulettePrize(Coins, 25),

            RoulettePrize(XP, 20),
            RoulettePrize(XP, 50),

            RoulettePrize(Airbox, 1),

            RoulettePrize(CoinBooster, 1),
            RoulettePrize(XPBooster, 1),

            RoulettePrize(SpinAgain, 1),
        )
        return prizes.shuffled()
    }

    fun wonPrize(roulettePrize: RoulettePrize) {
        val name = roulettePrize.type.name
        val quantity = roulettePrize.quantity
        val context = application.applicationContext
        Toast.makeText(context, context.getString(R.string.youWonPrize, quantity, name), Toast.LENGTH_SHORT).show()

        when (roulettePrize.type) {
            Coins -> ProfileApiInterfaceImplementation.addCoins(application, roulettePrize.quantity.toString())
            XP -> ProfileApiInterfaceImplementation.addXP(application, roulettePrize.quantity.toString())
            XPBooster -> ProfileApiInterfaceImplementation.buyItem(application, "exp_booster", roulettePrize.quantity, true)
            CoinBooster ->
                ProfileApiInterfaceImplementation.buyItem(application, "coin_booster", roulettePrize.quantity, true)
            Airbox -> ProfileApiInterfaceImplementation.buyItem(application, "airbox", roulettePrize.quantity, true)
            SpinAgain -> {
                _canSpin.value = true
                return
            }
        }
        _canSpin.value = false
        if (checkSpinned) {
            ProfileApiInterfaceImplementation.postSpin(application)
        }
    }
}
