package com.example.vkcourse.data.mapper

import com.example.vkcourse.domain.model.Category

/**
 * API присылает категории по-русски («Игры», «Фото и видео»), а доменная
 * модель использует enum. Маппинг терпим к английским значениям и регистру.
 */
internal object CategoryMapper {
    fun toDomain(apiValue: String?): Category {
        val value = apiValue?.trim()?.lowercase().orEmpty()
        return when (value) {
            "game", "games", "игры" -> Category.GAME
            "productivity", "производительность" -> Category.PRODUCTIVITY
            "social", "общение", "социальные сети" -> Category.SOCIAL
            "education", "образование" -> Category.EDUCATION
            "entertainment", "развлечения" -> Category.ENTERTAINMENT
            "music", "музыка" -> Category.MUSIC
            "video", "видео" -> Category.VIDEO
            "photography", "фото", "фото и видео" -> Category.PHOTOGRAPHY
            "health", "здоровье", "здоровье и фитнес" -> Category.HEALTH
            "sports", "спорт" -> Category.SPORTS
            "news", "новости" -> Category.NEWS
            "books", "книги", "книги и справочники" -> Category.BOOKS
            "business", "бизнес" -> Category.BUSINESS
            "finance", "финансы" -> Category.FINANCE
            "travel", "путешествия" -> Category.TRAVEL
            "maps", "навигация", "карты" -> Category.MAPS
            "food", "еда", "еда и напитки" -> Category.FOOD
            "shopping", "шопинг", "покупки" -> Category.SHOPPING
            "utilities", "утилиты", "погода" -> Category.UTILITIES
            else -> Category.entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: Category.APP
        }
    }
}
