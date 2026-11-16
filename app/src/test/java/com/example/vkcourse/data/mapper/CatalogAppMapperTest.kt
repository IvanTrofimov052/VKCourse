package com.example.vkcourse.data.mapper

import com.example.vkcourse.data.dto.CatalogAppDto
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.model.Category
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogAppMapperTest {
    private val mapper = CatalogAppMapper()
    private val dto = CatalogAppDto(
        id = "notes",
        name = "Notes",
        description = "Save notes and ideas",
        category = "Производительность",
        iconUrl = "https://example.com/icon.png",
    )

    @Test
    fun mapsEveryField() {
        assertEquals(
            CatalogApp(
                id = "notes",
                name = "Notes",
                description = "Save notes and ideas",
                category = Category.PRODUCTIVITY,
                iconUrl = "https://example.com/icon.png",
            ),
            mapper.toDomain(dto),
        )
    }

    @Test
    fun convertsRussianApiCategories() {
        assertEquals(Category.GAME, mapper.toDomain(dto.copy(category = "Игры")).category)
        assertEquals(
            Category.PRODUCTIVITY,
            mapper.toDomain(dto.copy(category = "Производительность")).category,
        )
        assertEquals(
            Category.PHOTOGRAPHY,
            mapper.toDomain(dto.copy(category = "Фото и видео")).category,
        )
        assertEquals(
            Category.HEALTH,
            mapper.toDomain(dto.copy(category = "Здоровье и фитнес")).category,
        )
        assertEquals(Category.MAPS, mapper.toDomain(dto.copy(category = "Навигация")).category)
    }

    @Test
    fun unknownCategoryFallsBackToApp() {
        assertEquals(Category.APP, mapper.toDomain(dto.copy(category = "Новая категория")).category)
    }
}
