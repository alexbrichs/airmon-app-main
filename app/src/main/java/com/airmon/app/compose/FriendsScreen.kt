package com.airmon.app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airmon.app.R
import com.airmon.app.viewmodels.FriendsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(viewModel: FriendsViewModel, navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.friends)) },
            actions = {
                IconButton(onClick = { navController.navigate("MainChatsScreen") }) {
                    Icon(Icons.Filled.Inbox, contentDescription = null)
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), contentAlignment = Alignment.TopCenter
        ) {
            Friends(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Friends(viewModel: FriendsViewModel, navController: NavController) {
    val friendNames: List<String> by viewModel.listFriends.collectAsState()
    SearchUsersBar(viewModel, navController)

    LazyVerticalGrid(
        modifier = Modifier.padding(vertical = 60.dp),
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(3.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(friendNames) { it ->
            infoUsuari(viewModel = viewModel, navController = navController, nom = it)
        }
    }
}

@Composable
fun infoUsuari(
    viewModel: FriendsViewModel,
    navController: NavController,
    nom: String
) {
    Column(
        Modifier.padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                navController.navigate("FriendInfo/$nom")
            }) {
            avatarUsuari(viewModel, nom)

            Spacer(modifier = Modifier.width(4.dp))
            Text(text = nom)
        }
        imatgesUsuari(viewModel, nom)
    }
}

@Composable
fun avatarUsuari(viewModel: FriendsViewModel, nom: String) {
    val avatar = viewModel.getAvatar(nom)
    avatar?.let {
        carregaAvatar(avatar)
    } ?: Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        modifier = Modifier.size(30.dp)
    )
}

@Composable
fun carregaAvatar(avatar: String) {
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

@Composable
fun imatgesUsuari(viewModel: FriendsViewModel, nom: String) {
    val fotos = viewModel.getFotos(nom)
    if (fotos.isNotEmpty()) {
        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fotos) {
                carregaImatge(it)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.notImages))
        }
    }
}

@Composable
fun carregaImatge(url: String) {
    val painter = rememberImagePainter(
        data = url,
        builder = {}
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(150.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsersBar(viewModel: FriendsViewModel, navController: NavController) {
    var usuaris by remember { mutableStateOf<List<String>>(emptyList()) }
    var text by remember { mutableStateOf<String>("") }
    var activa by remember { mutableStateOf<Boolean>(false) }
    var add_actiu by remember { mutableStateOf<Boolean>(false) }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
            usuaris = viewModel.busca_usuaris(it)
        },
        onSearch = {
            activa = false
            text = ""
        },
        active = activa,
        onActiveChange = {
            activa = it
        },
        placeholder = {
            Text(text = stringResource(R.string.searchUsers))
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (activa) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            activa = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        },
    ) {
        if (text.isNotEmpty()) {
            LazyColumn {
                items(usuaris) { usuari ->
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                        Text(text = usuari,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { navController.navigate("FriendInfo/$usuari") })
                        Spacer(modifier = Modifier.width(60.dp))
                        add_actiu = !viewModel.isFriends(usuari)
                        if (add_actiu) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Es pot afegir Icon",
                                modifier = Modifier.clickable {
                                    // CREAR AMISTAT
                                    viewModel.addFriend(usuari)
                                    add_actiu = !viewModel.isFriends(usuari)
                                }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.HorizontalRule,
                                contentDescription = "Desafegir Icon",
                                modifier = Modifier.clickable {
                                    // DESFER AMISTAT
                                    viewModel.unFriend(usuari)
                                    add_actiu = !viewModel.isFriends(usuari)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
