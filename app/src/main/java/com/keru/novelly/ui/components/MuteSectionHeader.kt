package com.keru.novelly.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MuteSectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .alpha(.75f)
            .padding(horizontal = 16.dp), fontSize = 12.sp
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MSHP() {
    MuteSectionHeader(title = "About Me")
}