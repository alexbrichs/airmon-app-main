package com.airmon.app.compose

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.models.Message
import com.airmon.app.viewmodels.ChatViewModel
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(viewModel: ChatViewModel, navController: NavController) {
    val messages by viewModel.messages.observeAsState()
    val listState = rememberLazyListState()
    val currentUser = viewModel.getCurrentUser()
    viewModel.startChat()

    Navigations(viewModel, navController)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileContainer(
                navController,
                viewModel,
                Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                messages?.let {
                    LazyColumn(
                        state = listState, modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 10.dp, end = 10.dp,
                                top = 25.dp, bottom = 80.dp
                            )
                    ) {
                        items(messages!!) { item ->
                            ChatContainer(item, currentUser!!)
                        }
                    }

                    LaunchedEffect(messages) {
                        listState.animateScrollToItem(max(messages!!.size - 1, 0))
                    }
                }

                SendMessageBar(
                    viewModel,
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SendMessageBar(viewModel: ChatViewModel, modifier: Modifier = Modifier) {
    var messageText by remember { mutableStateOf("") }

    TextField(
        value = messageText,
        onValueChange = { messageText = it }, shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                stringResource(R.string.typeAMessage),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                if (messageText.isNotBlank()) {
                    viewModel.addMessage(messageText)
                    messageText = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ChatContainer(
    message: Message,
    currentUser: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.sender == currentUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (message.sender == currentUser) Color.Blue else Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = message.content,
                color = if (message.sender == currentUser) Color.White else Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileContainer(
    navController: NavController,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    viewModel.closeChat()
                    navController.navigate("MainChatsScreen")
                },
            tint = Color.White
        )

        Row(modifier = Modifier.align(Alignment.Center)) {
            AvatarChat(viewModel, viewModel.name)

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = viewModel.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun Navigations(viewModel: ChatViewModel, navController: NavController) {
    val context = LocalContext.current

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun handleOnBackPressed() {
            viewModel.closeChat()
            navController.navigate("MainChatsScreen")
        }
    }

    LocalOnBackPressedDispatcherOwner.current
        ?.onBackPressedDispatcher
        ?.addCallback(onBackPressedCallback)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvatarChat(viewModel: ChatViewModel, nom: String) {
    val avatar = viewModel.getAvatar(nom)
    avatar?.let {
        CarregaAvatarChat(avatar)
    } ?: Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        modifier = Modifier.size(30.dp)
    )
}

@Composable
fun CarregaAvatarChat(avatar: String) {
    val painter = rememberImagePainter(
        data = avatar,
        builder = {}
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(30.dp)
    )
}
