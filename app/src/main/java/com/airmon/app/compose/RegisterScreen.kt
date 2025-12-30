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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
fun RegisterScreen(viewModel: RegisterViewModel, loginViewModel: LoginViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(SkyBlue)
    ) {
        Register(
            Modifier
                .align(Alignment.Center)
                .offset(y = (-25).dp),
            viewModel,
            loginViewModel
        )
    }
}

@Composable
fun Register(
    modifier: Modifier,
    viewModel: RegisterViewModel,
    loginViewModel: LoginViewModel
) {
    val username: String by viewModel.username.observeAsState(initial = "")
    val password1: String by viewModel.password1.observeAsState(initial = "")
    val password2: String by viewModel.password2.observeAsState(initial = "")
    val buttonEnable: Boolean by viewModel.buttonEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isLogged: Boolean by viewModel.isLogged.observeAsState(initial = false)
    val errorMessage: String by viewModel.errorMessage.observeAsState(initial = "")
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        LoadingScreen()
    } else if (isLogged) {
        val context = LocalContext.current
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
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
            Spacer(modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally))
            UsernameField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                username
            ) { viewModel.onLoginChanged(it, password1, password2) }
            Spacer(modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally))
            PasswordField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                stringResource(R.string.enterPassword),
                password1
            ) { viewModel.onLoginChanged(username, it, password2) }
            PasswordField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                stringResource(R.string.repeatPassword),
                password2
            ) { viewModel.onLoginChanged(username, password1, it) }
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

            RegisterButton(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp),
                stringResource(R.string.registerVerb),
                buttonEnable
            ) {
                coroutineScope.launch {
                    viewModel.onRegisterPressed()
                }
            }
            GoToLoginButton(
                Modifier
                    .align(Alignment.CenterHorizontally)
            ) { loginViewModel.showLoginScreen() }
        }
    }
}

@Composable
fun UsernameField(
    modifier: Modifier,
    username: String,
    onTextFieldChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = username,
        onValueChange = { onTextFieldChanged(it) },
        singleLine = true,
        placeholder = {
            Text(stringResource(R.string.username))
        }
    )
}

@Composable
fun PasswordField(
    modifier: Modifier,
    placeholder: String,
    password: String,
    onTextFieldChanged: (String) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = modifier,
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = "")
            }
        }
    )
}

@Composable
fun ErrorLabel(modifier: Modifier, errorMessage: String) {
    Text(
        modifier = modifier.widthIn(0.dp, 300.dp),
        text = errorMessage,
        textAlign = TextAlign.Center
    )
}

@Composable
fun RegisterButton(
    modifier: Modifier,
    text: String,
    loginEnabled: Boolean,
    onRegisterPressed: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = loginEnabled,
        onClick = { onRegisterPressed() },
        colors = ButtonDefaults.buttonColors(
            contentColor = if (loginEnabled) Color.White else Color.Gray,
            containerColor = if (loginEnabled) Color.Blue.copy(alpha = 0.5f) else Color.Transparent
        ),
        content = {
            Text(text)
        }
    )
}

@Composable
fun GoToLoginButton(modifier: Modifier, onLoginPressed: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = { onLoginPressed() },
        content = {
            Text(stringResource(R.string.alreadyHaveAccount))
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
