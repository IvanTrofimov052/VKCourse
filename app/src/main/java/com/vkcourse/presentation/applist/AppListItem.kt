package com.vkcourse.presentation.applist

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class AppListItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val icon: ImageVector,
    val iconColor: Color,
)
