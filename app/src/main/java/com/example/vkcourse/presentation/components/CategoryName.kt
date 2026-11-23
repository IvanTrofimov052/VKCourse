package com.example.vkcourse.presentation.components

import com.example.vkcourse.domain.model.Category

/** Человекочитаемое название категории для списка и карточки приложения. */
fun Category.displayName(): String = when (this) {
    Category.APP -> "Приложения"
    Category.GAME -> "Игры"
    Category.PRODUCTIVITY -> "Производительность"
    Category.SOCIAL -> "Общение"
    Category.EDUCATION -> "Образование"
    Category.ENTERTAINMENT -> "Развлечения"
    Category.MUSIC -> "Музыка"
    Category.VIDEO -> "Видео"
    Category.PHOTOGRAPHY -> "Фото и видео"
    Category.HEALTH -> "Здоровье и фитнес"
    Category.SPORTS -> "Спорт"
    Category.NEWS -> "Новости"
    Category.BOOKS -> "Книги и справочники"
    Category.BUSINESS -> "Бизнес"
    Category.FINANCE -> "Финансы"
    Category.TRAVEL -> "Путешествия"
    Category.MAPS -> "Навигация"
    Category.FOOD -> "Еда и напитки"
    Category.SHOPPING -> "Шопинг"
    Category.UTILITIES -> "Утилиты"
}
