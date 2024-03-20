package com.keru.novelly.ui.pages.more_page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GroupWork
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material.icons.rounded.RequestPage
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keru.novelly.R
import com.keru.novelly.ui.components.BottomNavBar
import com.keru.novelly.ui.components.DeleteAccountDialog
import com.keru.novelly.ui.components.EditProfileBottomSheet
import com.keru.novelly.ui.components.MuteSectionHeader
import com.keru.novelly.ui.components.SettingsCard
import com.keru.novelly.ui.navigation.Routes
import com.keru.novelly.utils.openLink
import com.keru.novelly.utils.rateApp
import com.keru.novelly.utils.sendEmail
import com.keru.novelly.utils.shareApp
import com.keru.novelly.utils.toastComingSoon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorePage(
    navController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState = vm.uiState
    val context = LocalContext.current
    val numberOfBooksRead = uiState.user?.completedBooks?.size ?: 0
    var isDeletingProfile by rememberSaveable {
        mutableStateOf(false)
    }
    var showEditProfile by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(bottomBar = {
        BottomNavBar(navController)
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
                .verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isUserSignedIn) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = uiState.user?.name ?: "...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                        IconButton(modifier = Modifier.align(Alignment.TopEnd),
                            onClick = { showEditProfile = !showEditProfile }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit, contentDescription = ""
                            )
                        }
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(context).data(uiState.user?.image)
                            .crossfade(true).build(),
                        contentDescription = "",
                        fallback = painterResource(id = R.drawable.user),
                        placeholder = painterResource(id = R.drawable.user),
                        error = painterResource(id = R.drawable.user),
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(50))
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.navigate(Routes.UserPage.path)
                            },
                        contentScale = ContentScale.Crop
                    )
                }

                MuteSectionHeader(title = "About Me")

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Routes.UserPage.path)
                            }) {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 16.dp, vertical = 8.dp
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiState.user?.name ?: "...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(14.dp)
                                        .alpha(.75f)
                                )
                            }
                        }
                        SettingsCard(
                            title = "Books read",
                            stats = "$numberOfBooksRead",
                            icon = Icons.Rounded.Book,
                        ) {
                            navController.navigate(Routes.CompletedBooksPage.path)
                        }

                        SettingsCard(
                            title = "Favorite Genre",
                            stats = uiState.favoriteGenre ?: "",
                            icon = Icons.Rounded.FavoriteBorder,
                            hasAction = false
                        ) {
                            context.toastComingSoon()
                        }
                    }
                }
            } else {
                ElevatedCard(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.books_pile),
                            contentDescription = "",
                            modifier = Modifier
                                .width(72.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit,
                        )
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Please sign in to be able to unlock all the wonders of the novelly app.",
                                fontSize = 12.sp
                            )
                            Button(
                                onClick = {
                                    navController.navigate(Routes.LoginPage.path)
                                }, modifier = Modifier.fillMaxWidth(.5f)
                            ) {
                                Text(text = "Sign In")
                            }
                        }
                    }

                }
            }

            //DarModeSwitcher {}
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsCard(title = "Settings", icon = Icons.Rounded.Settings) {
                        navController.navigate(Routes.SettingsPage.path)
                    }
                    SettingsCard(title = "Upload book", icon = Icons.Outlined.CloudUpload) {
                        navController.navigate(Routes.UploadBookPage.path)
                    }
                    SettingsCard(title = "Request a Book", icon = Icons.Rounded.RequestPage) {
                        context.sendEmail(
                            subject = "Book Request",
                            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ©️Novelly"
                        )
                    }
                    SettingsCard(title = "Feedback", icon = Icons.Rounded.Mail) {
                        context.sendEmail(
                            subject = "App Feedback",
                            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ©️Novelly"
                        )
                    }
                    SettingsCard(title = "Invite", icon = Icons.Rounded.Share) {
                        context.shareApp()
                    }
                    SettingsCard(title = "Rate App", icon = Icons.Rounded.Star) {
                        context.rateApp()
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsCard(title = "Donate", icon = Icons.Rounded.CardGiftcard) {
                        context.toastComingSoon()
                    }
                    SettingsCard(
                        title = "Join Our Telegram Community", icon = Icons.Rounded.PeopleAlt
                    ) {
                        context.openLink("https://t.me/chapterly_app")
                    }
                    SettingsCard(
                        title = "Join Our Discord Community", icon = Icons.Rounded.GroupWork
                    ) {
                        context.openLink("https://discord.com/invite/6PjJtNJy5w")
                    }
                    SettingsCard(title = "About App", icon = Icons.Rounded.Info) {
                        navController.navigate(Routes.AboutAppPage.path)
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    AnimatedVisibility(visible = vm.uiState.isUserSignedIn) {
                        SettingsCard(title = "Change Password", icon = Icons.Rounded.LockOpen) {
                            navController.navigate(Routes.RecoverPasswordPage.path)
                        }
                    }
                    SettingsCard(
                        title = if (vm.uiState.isUserSignedIn) "Sign Out" else "Sign In",
                        icon = if (vm.uiState.isUserSignedIn) Icons.AutoMirrored.Rounded.Logout else Icons.AutoMirrored.Rounded.Login
                    ) {
                        if (vm.uiState.isUserSignedIn) vm.signOut()
                        else navController.navigate(
                            Routes.LoginPage.path
                        )
                    }
                    AnimatedVisibility(visible = vm.uiState.isUserSignedIn) {
                        SettingsCard(title = "Delete Account", icon = Icons.Rounded.Delete) {
                            isDeletingProfile = !isDeletingProfile
                        }
                    }

                }
            }
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


    if (isDeletingProfile) {
        Dialog(onDismissRequest = {
            isDeletingProfile = false
        }) {
            DeleteAccountDialog(onClick = { vm.deleteUser() }) {
                isDeletingProfile = false

            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview() {
    Column(
        modifier = Modifier.padding(0.dp),
    ) {
        SettingsCard(title = "Settings", icon = Icons.Rounded.Settings) {}
        SettingsCard(title = "Change Password", icon = Icons.Rounded.LockOpen) {}
        SettingsCard(
            title = "Favorite Genre", stats = "Fiction", icon = Icons.Rounded.FavoriteBorder
        ) {}
        SettingsCard(title = "Books read", stats = "3", icon = Icons.Rounded.Book) {}
        SettingsCard(title = "Logout", icon = Icons.AutoMirrored.Rounded.Logout) {}
    }
}