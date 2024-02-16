package com.keru.novelly.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keru.novelly.data.data_source.local.models.genreDetailsList
import com.keru.novelly.ui.theme.NovellyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresPage(onSelected: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genreDetailsList) {
            ElevatedCard(
                onClick = { onSelected(it.name) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = it.emoji)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = it.name)
                }
            }
        }

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GPP() {
    NovellyTheme {
        GenresPage {

        }
    }
}