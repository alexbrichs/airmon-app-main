package com.airmon.app

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

import com.airmon.app.compose.LoginScreen
import com.airmon.app.ui.theme.AirmonAppTheme
import com.airmon.app.viewmodels.LoginViewModel

class LoginActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initializeContent(application)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AirmonAppTheme {
                if (ContextCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    initializeContent(application)
                }
            }
        }
    }

    private fun initializeContent(application: Application) {
        setContent {
            AirmonAppTheme {
                LoginScreen(
                    viewModel = LoginViewModel(application)
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ca")
@Composable
private fun GreetingPreview() {
    AirmonAppTheme {
        LoginScreen(viewModel = LoginViewModel(Application()))
    }
}
