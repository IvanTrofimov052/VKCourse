package com.vkcourse.presentation.applist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color

internal val sampleApps = listOf(
    AppListItem(
        id = "vk",
        name = "ВКонтакте",
        description = "Общайтесь с друзьями, делитесь моментами и находите сообщества по интересам.",
        category = "Социальные сети",
        icon = Icons.Default.AccountCircle,
        iconColor = Color(0xFF0077FF),
    ),
    AppListItem(
        id = "music",
        name = "VK Музыка",
        description = "Любимые треки, подкасты и плейлисты под ваше настроение.",
        category = "Музыка",
        icon = Icons.Default.PlayArrow,
        iconColor = Color(0xFF8854E8),
    ),
    AppListItem(
        id = "maps",
        name = "Карты",
        description = "Ищите интересные места и стройте удобные маршруты по городу.",
        category = "Карты и навигация",
        icon = Icons.Default.LocationOn,
        iconColor = Color(0xFF249D76),
    ),
    AppListItem(
        id = "notes",
        name = "Заметки",
        description = "Сохраняйте идеи, составляйте списки и планируйте каждый день.",
        category = "Производительность",
        icon = Icons.AutoMirrored.Filled.List,
        iconColor = Color(0xFFE69B24),
    ),
    AppListItem(
        id = "mail",
        name = "Почта Mail",
        description = "Все письма и почтовые ящики в одном удобном приложении.",
        category = "Общение",
        icon = Icons.Default.Email,
        iconColor = Color(0xFF247ACD),
    ),
    AppListItem(
        id = "calendar",
        name = "Календарь",
        description = "Планируйте встречи и важные события с напоминаниями.",
        category = "Производительность",
        icon = Icons.Default.DateRange,
        iconColor = Color(0xFFE06073),
    ),
    AppListItem(
        id = "favorites",
        name = "Интересное",
        description = "Открывайте подборки фильмов, книг и новых увлечений.",
        category = "Развлечения",
        icon = Icons.Default.Star,
        iconColor = Color(0xFF665CD6),
    ),
    AppListItem(
        id = "health",
        name = "Здоровье",
        description = "Следите за активностью и формируйте полезные привычки.",
        category = "Здоровье и фитнес",
        icon = Icons.Default.Favorite,
        iconColor = Color(0xFFCB4D70),
    ),
)
