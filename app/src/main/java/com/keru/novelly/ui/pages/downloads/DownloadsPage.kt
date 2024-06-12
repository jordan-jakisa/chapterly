package com.keru.novelly.ui.pages.downloads

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keru.novelly.R
import com.keru.novelly.ui.components.BottomNavBar
import com.keru.novelly.ui.components.CurrentlyReadingCard
import com.keru.novelly.ui.components.DownloadedBookItem
import com.keru.novelly.ui.components.PageHeader
import com.keru.novelly.ui.pages.pdf_reader.ReaderActivity
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DownloadsPage(
    navController: NavController,
    vm: DownloadsViewModel = hiltViewModel()
) {
    val uiState = vm.uiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        vm.getDownloadedBooks()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PageHeader(title = "My Library")

            AnimatedVisibility(visible = uiState.booksInProgress.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Currently Reading")
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        uiState.booksInProgress.forEach { downloadedFile ->
                            val deleteAction = SwipeAction(
                                icon = rememberVectorPainter(image = Icons.Rounded.DeleteOutline),
                                background = Color.Red,
                                onSwipe = {
                                    downloadedFile.file.delete()
                                    vm.refreshList()
                                }
                            )

                            //swipeable item
                            SwipeableActionsBox(
                                modifier = Modifier.fillMaxWidth(),
                                startActions = listOf(deleteAction),
                                endActions = listOf(deleteAction)
                            ) {
                                CurrentlyReadingCard(book = downloadedFile) {
                                    val intent = Intent(context, ReaderActivity::class.java)
                                    intent.putExtra(
                                        "bookUri",
                                        downloadedFile.file.toUri().toString()
                                    )
                                    startActivity(context, intent, null)
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(visible = uiState.books.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Downloaded")
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        vm.uiState.books.let {
                            it.forEach { file ->
                                val deleteAction = SwipeAction(
                                    icon = rememberVectorPainter(image = Icons.Rounded.DeleteOutline),
                                    background = Color.Red,
                                    onSwipe = {
                                        file.delete()
                                        vm.refreshList()
                                    }
                                )

                                //swipeable item
                                SwipeableActionsBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    startActions = listOf(deleteAction),
                                    endActions = listOf(deleteAction)
                                ) {
                                    DownloadedBookItem(
                                        file = file,
                                    ) {
                                        val intent = Intent(context, ReaderActivity::class.java)
                                        intent.putExtra("bookUri", file.toUri().toString())
                                        startActivity(context, intent, null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = uiState.books.isEmpty() && uiState.booksInProgress.isEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.serving_dish),
                            contentDescription = "No books downloaded yet!",
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Nothing here yet! \nBooks you download will appear here!",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}