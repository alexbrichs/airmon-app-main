package com.airmon.app.compose

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.airmon.app.R
import com.airmon.app.ui.theme.CustomTypography
import com.airmon.app.ui.theme.InfoTypography
import com.airmon.app.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    context: Context
) {
    val password: String by viewModel.password1.observeAsState(initial = "")
    val password2: String by viewModel.password2.observeAsState(initial = "")
    var uriAvatar: Uri? by remember { mutableStateOf(null) }
    var displayString by remember { mutableStateOf<String>("") }
    val errorMessage: String by viewModel.errorMessage.observeAsState(initial = "")
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.settings)) },
        )

        // Espacio entre los elementos
        Spacer(modifier = Modifier.height(16.dp))

        // Columna para el primer campo de contraseña
        Column {
            Text(
                text = stringResource(R.string.password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(modifier = Modifier.fillMaxWidth(), stringResource(R.string.newPassword), password) {
                viewModel.onPassword1Change(it)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Columna para el segundo campo de contraseña
        Column {
            Text(
                text = stringResource(R.string.confirmNewPassword)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(modifier = Modifier.fillMaxWidth(), stringResource(R.string.newPassword), password2) {
                viewModel.onPassword2Change(it)
            }
        }

        // Espacio entre los elementos
        Spacer(modifier = Modifier.height(30.dp))

        // Columna para el selector de imagen
        Column {
            Text(
                text = stringResource(R.string.avatar)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ImagePickerRow() {uri ->
                uriAvatar = uri
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                viewModel.guardarPassword(password, password2, uriAvatar ?: null)
                displayString = context.resources.getString(R.string.confirmedChanges)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Guardar")
        }

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
        } else {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = displayString,
                    style = InfoTypography.titleMedium.plus(TextStyle(color = Color.Green))
                )
            }
        }
    }
}

@Composable
fun ImagePickerRow(
    onImageSelected: (Uri) -> Unit
) {
    val photoLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    onImageSelected(uri)
                }
            })
    Row(
        modifier = Modifier
            .clickable {
                photoLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCellIcon(Icons.Filled.PhotoCamera, 0.3f)
        Spacer(modifier = Modifier.width(16.dp))
        // Utiliza un Text con modificador Modifier.weight(1f) para ajustar el texto al ancho disponible
        Text(
            text = stringResource(R.string.selectNewAvatar),
            modifier = Modifier.weight(1f),
            style = CustomTypography.displaySmall,
            textAlign = TextAlign.Left
        )
    }
}
