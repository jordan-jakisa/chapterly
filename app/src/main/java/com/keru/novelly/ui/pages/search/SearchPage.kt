package com.keru.novelly.ui.pages.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.keru.novelly.ui.components.BottomNavBar
import com.keru.novelly.ui.components.GenresPage
import com.keru.novelly.ui.components.PageHeader
import com.keru.novelly.ui.components.SearchResultsPage
import com.keru.novelly.ui.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchPage(
    navController: NavController, vm: SearchViewModel = hiltViewModel()
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState {
        tabData.size
    }
    val scope = rememberCoroutineScope()

    Scaffold(bottomBar = {
        BottomNavBar(navController)
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            PageHeader(title = "Explore")
            DockedSearchBar(
                placeholder = { Text(text = "Title, author & genre") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                },
                onSearch = {
                    if (it.isNotEmpty()) {
                        vm.searchBooks(it)
                        searchQuery = ""
                    }
                },
                active = searchQuery.isNotEmpty(),
                onActiveChange = {}) {
                vm.uiState.books?.let {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        it.forEach { book ->
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    val path = Routes.BookDetails.path.replace("{bookId}", book.bid)
                                    navController.navigate(path)
                                }) {
                                Text(
                                    text = book.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(text = book.authorName, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage, divider = {
                        Spacer(modifier = Modifier.height(5.dp))
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp), edgePadding = 16.dp
                ) {
                    tabData.forEachIndexed { index, tabItem ->
                        Tab(selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = {
                                Text(
                                    text = tabItem.name,
                                )
                            })
                    }
                }

                HorizontalPager(
                    state = pagerState, modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    when (it) {
                        0 -> {
                            SearchResultsPage(
                                searchResults = vm.uiState.books,
                                suggestedBooks = vm.uiState.explorationBooks,
                                onBookClicked = { bookId ->
                                    val path = Routes.BookDetails.path.replace(
                                        "{bookId}", bookId
                                    )
                                    navController.navigate(path)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        1 -> {
                            GenresPage(
                                onSelected = { query ->
                                    vm.searchBooks(query)
                                    scope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            )
                        }

                        2 -> {
                            ElevatedCard {
                                Text(text = "Coming Soon", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SPP() {
    SearchPage(rememberNavController())
}