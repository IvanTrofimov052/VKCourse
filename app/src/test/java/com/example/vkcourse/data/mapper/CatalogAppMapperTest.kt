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
    private val domain = CatalogApp(
        id = "notes",
        name = "Notes",
        description = "Save notes and ideas",
        category = Category.PRODUCTIVITY,
        iconUrl = "https://example.com/icon.png",
    )

    @Test
    fun mapsEveryField() {
        assertEquals(domain, mapper.toDomain(dto))
    }

    @Test
    fun mapsEveryDomainFieldToDto() {
        assertEquals(dto, mapper.toDto(domain))
    }

    @Test
    fun domainToDtoToDomainRoundTripKeepsModel() {
        assertEquals(domain, mapper.toDomain(mapper.toDto(domain)))
    }

    @Test
    fun dtoToDomainToDtoRoundTripKeepsEveryField() {
        val mapped = mapper.toDto(mapper.toDomain(dto))

        assertEquals(dto.id, mapped.id)
        assertEquals(dto.name, mapped.name)
        assertEquals(dto.description, mapped.description)
        assertEquals(dto.iconUrl, mapped.iconUrl)
    }

    @Test
    fun reverseMappingUsesRussianApiCategoryNames() {
        assertEquals("Игры", mapper.toDto(domain.copy(category = Category.GAME)).category)
        assertEquals(
            "Производительность",
            mapper.toDto(domain.copy(category = Category.PRODUCTIVITY)).category,
        )
        assertEquals(
            "Фото и видео",
            mapper.toDto(domain.copy(category = Category.PHOTOGRAPHY)).category,
        )
        assertEquals(
            "Здоровье и фитнес",
            mapper.toDto(domain.copy(category = Category.HEALTH)).category,
        )
        assertEquals("Навигация", mapper.toDto(domain.copy(category = Category.MAPS)).category)
    }

    @Test
    fun everyCategorySurvivesDtoRoundTrip() {
        Category.entries.forEach { category ->
            val mapped = mapper.toDomain(mapper.toDto(domain.copy(category = category)))

            assertEquals(category, mapped.category)
        }
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
