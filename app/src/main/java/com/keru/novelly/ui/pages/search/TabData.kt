package com.keru.novelly.ui.pages.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DonutSmall
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
    val name: String,
    val icon: ImageVector
)

val tabData = listOf<TabItem>(
    TabItem("All", Icons.Rounded.DonutSmall),
    TabItem(name = "Category", icon = Icons.Rounded.Category),
    TabItem(name = "Authors", icon = Icons.Rounded.PersonSearch),
)