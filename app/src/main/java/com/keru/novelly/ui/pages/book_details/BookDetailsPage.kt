package com.keru.novelly.ui.pages.book_details

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keru.novelly.R
import com.keru.novelly.ui.theme.NovellyTheme
import com.keru.novelly.utils.shareContent
import com.keru.novelly.utils.toastComingSoon
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookDetailsPage(
    bookId: String?,
    navController: NavController,
    vm: BookDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val downloads = vm.uiState.book?.downloads ?: 0
    val uiState = vm.uiState
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLiked by rememberSaveable {
        mutableStateOf(false)
    }
    val hazeState = remember { HazeState() }
    val pagerState = rememberPagerState {
        2
    }
    val rating = uiState.book?.rating ?: 0f

    LaunchedEffect(key1 = Unit) {
        if (vm.uiState.book == null) vm.getBook(bookId ?: "")
    }

    LaunchedEffect(key1 = vm.uiState.isLiked) {
        isLiked = vm.uiState.isLiked
    }

    LaunchedEffect(key1 = uiState.error) {
        if (uiState.error != null) scope.launch {
            snackbarHostState.showSnackbar(uiState.error)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()

        ) {

            AsyncImage(
                model = ImageRequest.Builder(context).data(uiState.book?.image)
                    .crossfade(true).build(),
                contentDescription = "",
                fallback = painterResource(id = R.drawable.luffy),
                error = painterResource(id = R.drawable.luffy),
                placeholder = painterResource(id = R.drawable.luffy),
                modifier = Modifier
                    .fillMaxSize()
                    .haze(
                        state = hazeState,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        tint = MaterialTheme.colorScheme.surface.copy(
                            alpha = .3f
                        ),
                        blurRadius = 32.dp
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeChild(
                        state = hazeState
                    )
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = ""
                            )
                        }
                        Text(text = "")
                        Row {
                            IconButton(
                                onClick = {
                                    if (uiState.isUserSignedIn) {
                                        if (uiState.isLiked) vm.disLikeBook(uiState.book!!.bid)
                                        else vm.likeBook(uiState.book!!.bid)
                                        isLiked = !isLiked
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please, sign in!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }) {
                                Icon(
                                    painter = painterResource(
                                        id =
                                        if (isLiked) R.drawable.baseline_favorite_24
                                        else R.drawable.baseline_favorite_border_24
                                    ),
                                    contentDescription = "",
                                    tint = if (isLiked) Color.Red else Color.LightGray
                                )
                            }
                            IconButton(onClick = {
                                context.shareContent(
                                    uiState.book!!.description,
                                    uiState.book.title,
                                    uiState.book.book
                                )
                            }) {
                                Icon(imageVector = Icons.Rounded.Share, contentDescription = "")
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(uiState.book?.image)
                            .crossfade(true).build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(.55f)
                            .aspectRatio(4 / 5f)
                            .clip(RoundedCornerShape(16.dp)),
                        fallback = painterResource(id = R.drawable.bg_home_large),
                        error = painterResource(id = R.drawable.bg_home_large),
                        placeholder = painterResource(id = R.drawable.bg_home_large),
                    )
                    Surface(
                        modifier = Modifier
                            .padding(
                                top = 24.dp
                            ),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = vm.uiState.book?.title ?: "...",
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = vm.uiState.book?.authorName ?: "...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = .75f
                                )
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                            text = "$rating",
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Rating",
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
                                            text = "$downloads",
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Downloads",
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
                                            text = uiState.book?.category ?: "...",
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Genre",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = .75f
                                            )
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            ScrollableTabRow(
                                selectedTabIndex = pagerState.currentPage,
                                divider = {
                                    Spacer(modifier = Modifier.height(5.dp))
                                },
                                edgePadding = 0.dp,
                                indicator = {}
                            ) {
                                listOf("Synopsis", "Reviews").forEachIndexed { index, s ->
                                    Tab(selected = pagerState.currentPage == index,
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = s,
                                            fontSize = if (pagerState.currentPage == index) 14.sp else 12.sp,
                                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                                        )

                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                            ) {
                                when (it) {
                                    0 -> {
                                        ElevatedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 16.dp)
                                                .wrapContentHeight()
                                        ) {
                                            Text(
                                                text = vm.uiState.book?.description ?: "...",
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    }

                                    1 -> {
                                        ElevatedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 16.dp)
                                        ) {


                                            Text(
                                                text = "Coming soon", fontSize = 12.sp,
                                                modifier = Modifier.padding(16.dp)
                                            )

                                        }
                                    }
                                }

                            }
                            Spacer(
                                modifier = Modifier
                                    .height(360.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .align(Alignment.BottomStart)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                vm.uiState.book?.let {
                                    vm.downloadBook(it)
                                }
                            }, modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Download")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedButton(
                            onClick = {
                                context.toastComingSoon()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Buy")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BDPP() {
    NovellyTheme {
        BookDetailsPage(
            "", navController = rememberNavController()
        )
    }
}