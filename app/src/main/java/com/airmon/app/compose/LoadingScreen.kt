package com.airmon.app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.airmon.app.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = rememberAsyncImagePainter(
                model = LocalContext.current.packageManager
                    .getApplicationIcon("com.airmon.app")
            ),
            contentDescription = "App Logo",
            modifier = modifier
                .size(125.dp),
        )
        Text(
            text = stringResource(R.string.welcomeText),
            modifier = modifier
                .offset(y = 25.dp),
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        CircularProgressIndicator(
            modifier = Modifier.then(Modifier.size(60.dp))
        )
    }
}
