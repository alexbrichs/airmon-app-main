package com.airmon.app.compose

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.airmon.app.MainActivity
import com.airmon.app.R
import com.airmon.app.ui.theme.SkyBlue
import com.airmon.app.viewmodels.LoginViewModel
import com.airmon.app.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(SkyBlue)
    ) {
        Login(
            Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp),
            viewModel
        )
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {
    val username: String by viewModel.username.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val isLogged: Boolean by viewModel.isLogged.observeAsState(initial = false)
    val errorMessage: String by viewModel.errorMessage.observeAsState(initial = "")
    val isRegistering: Boolean by viewModel.isRegistering.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = "") {
        viewModel.checkStoredCredentials()
    }

    if (isLoading) {
        LoadingScreen()
    } else if (isLogged) {
        val context = LocalContext.current
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    } else if (isRegistering) {
        RegisterScreen(RegisterViewModel(viewModel.application), viewModel)
    } else {
        Column(modifier = modifier) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = LocalContext.current.packageManager
                        .getApplicationIcon("com.airmon.app")
                ),
                contentDescription = "App Logo",
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .size(125.dp),
            )
            Spacer(modifier = Modifier.padding(16.dp))
            UsernameField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                username
            ) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(16.dp))
            PasswordField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                stringResource(id = R.string.password),
                password
            ) { viewModel.onLoginChanged(username, it) }
            Spacer(modifier = Modifier.padding(16.dp))
            if (errorMessage.isNotEmpty()) {
                ErrorLabel(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .border(1.dp, Color.Red)
                        .background(Color.Red.copy(alpha = 0.2f))
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
                    errorMessage
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
            LoginButton(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp),
                loginEnable
            ) {
                coroutineScope.launch {
                    viewModel.onLoginPressed()
                }
            }
            GoToRegisterButton(
                Modifier
                    .align(Alignment.CenterHorizontally)
            ) { viewModel.onRegisterPressed() }
        }
    }
}

@Composable
fun LoginButton(
    modifier: Modifier,
    loginEnabled: Boolean,
    onLoginPressed: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = loginEnabled,
        onClick = { onLoginPressed() },
        colors = ButtonDefaults.buttonColors(
            contentColor = if (loginEnabled) Color.White else Color.Gray,
            containerColor = if (loginEnabled) Color.Blue.copy(alpha = 0.5f) else Color.Transparent
        ),
        content = {
            Text(stringResource(R.string.loginVerb))
        }
    )
}

@Composable
fun GoToRegisterButton(modifier: Modifier, onPressed: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = { onPressed() },
        content = {
            Text(stringResource(R.string.dontHaveAccount))
        }
    )
}

@Preview()
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        viewModel = LoginViewModel(application = Application())
    )
}
