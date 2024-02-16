package com.keru.novelly.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.keru.novelly.R
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.ui.theme.NovellyTheme

@Composable
fun BookCard(
    book: Book, onClick: (String) -> Unit
) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context).data(book.image).crossfade(true).build(),
        contentDescription = "",
        modifier = Modifier
            .clickable {
                onClick(book.bid)
            }
            .width(135.dp)
            .aspectRatio(3 / 4f)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.bg_home_large),
        error = painterResource(id = R.drawable.bg_home_large),
        fallback = painterResource(id = R.drawable.bg_home_large),
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BCP() {
    NovellyTheme {
        BookCard(book = Book()) {}
    }
}