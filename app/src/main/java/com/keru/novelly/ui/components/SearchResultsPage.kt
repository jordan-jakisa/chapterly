package com.keru.novelly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keru.novelly.data.data_source.local.models.Book

@Composable
fun SearchResultsPage(
    searchResults: List<Book>?,
    suggestedBooks: List<Book>?,
    onBookClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (searchResults != null) {
            AnimatedVisibility(visible = searchResults.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Search Results")
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        searchResults.forEach { book ->
                            SmallBookCard(
                                book
                            ) {
                                onBookClicked(book.bid)
                            }
                        }
                    }
                }
            }
        }
        if (suggestedBooks != null) {
            AnimatedVisibility(suggestedBooks.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 32.dp)
                        ) {
                            Text(
                                text = "Not found the book you are looking for? ",
                                fontSize = 14.sp
                            )
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(
                                    text = "Request for a book",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Explore")
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        suggestedBooks.forEach { book ->
                            SmallBookCard(
                                book
                            ) {
                                onBookClicked(book.bid)
                            }
                        }
                    }
                }
            }
        }
    }
}