package com.example.vkcourse.data.dto

/**
 * Элемент ответа GET /catalog. В каталоге приходит краткая информация:
 * подробности (разработчик, возраст, размер, скриншоты) отдаёт отдельный запрос.
 */
data class CatalogAppDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String,
)
