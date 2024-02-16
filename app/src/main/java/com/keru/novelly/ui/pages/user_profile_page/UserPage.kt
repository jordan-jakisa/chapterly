package com.keru.novelly.ui.pages.user_profile_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keru.novelly.R
import com.keru.novelly.ui.components.EditProfileBottomSheet
import com.keru.novelly.ui.components.MuteSectionHeader
import com.keru.novelly.ui.pages.more_page.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    val uiState = vm.uiState
    val likedBooks = uiState.user?.likedBooks?.size ?: 0
    val completedBooks = uiState.user?.completedBooks?.size ?: 0
    var showEditProfile by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(uiState.user?.image).crossfade(true)
                        .build(),
                    contentDescription = "",
                    fallback = painterResource(id = R.drawable.user),
                    placeholder = painterResource(id = R.drawable.user),
                    error = painterResource(id = R.drawable.user),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(50))
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.user?.name ?: "...",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiState.user?.email ?: "...",
                )
                ElevatedButton(onClick = { showEditProfile = !showEditProfile }) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Edit Profile")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                        topStart = 16.dp,
                        bottomStart = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = likedBooks.toString(),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Liked Books",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .75f
                            )
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(0.dp)

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = completedBooks.toString(),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Books read",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .75f
                            )
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.favoriteGenre ?: "0",
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Favorite Genre",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .75f
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            MuteSectionHeader(title = "Recommended Books")
        }

        if (showEditProfile) {
            EditProfileBottomSheet(user = uiState.user, onSave = { uri, name ->
                vm.updateUserDetails(uri, name)
                showEditProfile = !showEditProfile
            }, onDismiss = {
                showEditProfile = !showEditProfile
            }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun USP() {
    UserScreen(navController = rememberNavController())
}