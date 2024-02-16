package com.keru.novelly.ui.pages.home

import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.keru.novelly.R
import com.keru.novelly.ui.components.BookCard
import com.keru.novelly.ui.components.BottomNavBar
import com.keru.novelly.ui.components.PageHeader
import com.keru.novelly.ui.components.SectionHeader
import com.keru.novelly.ui.components.SmallBookCard
import com.keru.novelly.ui.navigation.Routes
import com.keru.novelly.ui.theme.NovellyTheme

@Composable
fun HomePage(
    navController: NavController, vm: HomePageViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState = vm.uiState
    val completedBooks =
        if (vm.uiState.user != null) vm.uiState.user?.completedBooks?.size ?: 0 else 0
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
        }
    }

    Scaffold(bottomBar = {
        BottomNavBar(navController)
    }, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PageHeader(title = "Dashboard")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 32.dp, end = 16.dp)
                        .clickable {
                            navController.navigate(Routes.Search.path)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search, contentDescription = "Search"
                    )
                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )
                    Text(
                        text = "Title, Authors or Genre",
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                            .padding(end = 16.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(.6f), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                            alpha = .5f
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 32.sp, fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("${uiState.currentStreak}")
                                }
                                withStyle(SpanStyle(fontSize = 12.sp)) {
                                    append("days")
                                }
                            })
                            Text(text = "Current Streak", fontSize = 12.sp)
                        }
                        Image(
                            painter = painterResource(
                                id = if (uiState.currentStreak == 0) R.drawable.sad
                                else R.drawable.happy
                            ), contentDescription = "", modifier = Modifier.size(46.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(.4f)
                        .clickable {
                            if (Firebase.auth.currentUser != null) navController.navigate(Routes.CompletedBooksPage.path)
                        }, colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 32.sp, fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("$completedBooks")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append(" books")
                            }
                        })
                        Text(text = "Completed", fontSize = 12.sp)
                    }
                }
            }

            SectionHeader(title = "Explore") {
                navController.navigate(Routes.Search.path)
            }

            vm.uiState.explorationBooks?.let {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    itemsIndexed(it) { index, book ->
                        BookCard(book = book, onClick = {
                            val path = Routes.BookDetails.path.replace("{bookId}", book.bid)
                            navController.navigate(path)
                        })
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Popular")
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                vm.uiState.popularBooks?.let {
                    it.forEach { book ->
                        SmallBookCard(
                            book
                        ) {
                            val path = Routes.BookDetails.path.replace("{bookId}", book.bid)
                            navController.navigate(path)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HPP() {
    NovellyTheme {
        HomePage(
            navController = rememberNavController()
        )
    }
}