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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.viewmodels.MainChatsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainChatsScreen(
    viewModel: MainChatsViewModel,
    navController: NavController,
    friendsScreen: String
) {
    ResetBackButton(navController)
    val searchText: MutableState<String> = remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        navController.navigate(friendsScreen)
                    }
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.chats),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = Color.Black
            )
        }
        SearchBar(searchText)

        Chats(viewModel, navController, searchText)
    }
}

@Composable
fun ResetBackButton(navController: NavController) {
    val originalOnBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(originalOnBackPressedCallback)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatItem(
    friend: String,
    navController: NavController,
    read: Boolean,
    viewModel: MainChatsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .offset(x = (-8).dp)
            .clickable { navController.navigate("ChatScreen/$friend") },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            AvatarAmic(viewModel, friend)

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = friend,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxHeight()
            )
        }

        if (!read) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chats(
    viewModel: MainChatsViewModel,
    navController: NavController,
    searchText: MutableState<String>
) {
    val friendNames: List<String> by viewModel.listFriends.collectAsState()

    LazyColumn(
        Modifier
            .padding(16.dp),
    ) {
        items(
            friendNames.sorted()
                .filter { it.contains(searchText.value, ignoreCase = true) }) { friend ->
            val readStatus = viewModel.isReadStatus(friend)
            ChatItem(friend, navController, readStatus, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: MutableState<String>) {
    TextField(
        value = searchText.value,
        onValueChange = { searchText.value = it },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        placeholder = {
            Text(
                stringResource(R.string.search),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        },
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvatarAmic(viewModel: MainChatsViewModel, nom: String) {
    val avatar = viewModel.getAvatar(nom)
    avatar?.let {
        CarregaAvatarAmic(avatar)
    } ?: Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        modifier = Modifier.size(30.dp)
    )
}

@Composable
fun CarregaAvatarAmic(avatar: String) {
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
