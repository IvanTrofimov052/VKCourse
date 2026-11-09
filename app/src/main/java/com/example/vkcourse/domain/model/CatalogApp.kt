package com.example.vkcourse.domain.model

/**
 * Краткая карточка приложения из каталога. Этого набора полей достаточно
 * для списка и витрины, подробная информация приходит отдельным запросом.
 */
data class CatalogApp(
    val id: String,
    val name: String,
    val description: String,
    val category: Category,
    val iconUrl: String,
)
