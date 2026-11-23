package com.example.vkcourse.data.mapper

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.Category
import org.junit.Assert.assertEquals
import org.junit.Test

class AppDetailsMapperTest {
    private val mapper = AppDetailsMapper()
    private val dto = AppDetailsDto(
        id = "notes",
        name = "Notes",
        developer = "Notes Studio",
        category = "productivity",
        ageRating = 6,
        size = 18.4f,
        iconUrl = "https://example.com/icon.png",
        screenshotUrlList = listOf("https://example.com/screenshot.png"),
        description = "Save notes and ideas",
    )
    private val domain = AppDetails(
        id = "notes",
        name = "Notes",
        developer = "Notes Studio",
        category = Category.PRODUCTIVITY,
        ageRating = 6,
        size = 18.4f,
        iconUrl = "https://example.com/icon.png",
        screenshotUrlList = listOf("https://example.com/screenshot.png"),
        description = "Save notes and ideas",
    )

    @Test
    fun mapsEveryFieldAndConvertsCategoryToDomainEnum() {
        assertEquals(domain, mapper.toDomain(dto))
    }

    @Test
    fun mapsEveryDomainFieldToDto() {
        val expected = dto.copy(category = "Производительность")

        assertEquals(expected, mapper.toDto(domain))
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
        assertEquals(dto.developer, mapped.developer)
        assertEquals(dto.ageRating, mapped.ageRating)
        assertEquals(dto.size, mapped.size)
        assertEquals(dto.iconUrl, mapped.iconUrl)
        assertEquals(dto.screenshotUrlList, mapped.screenshotUrlList)
        assertEquals(dto.description, mapped.description)
    }

    @Test
    fun missingScreenshotsBecomeEmptyDomainList() {
        val app = mapper.toDomain(dto.copy(screenshotUrlList = null))

        assertEquals(emptyList<String>(), app.screenshotUrlList)
    }

    @Test
    fun emptyScreenshotsStayEmptyAfterReverseMapping() {
        val mapped = mapper.toDto(domain.copy(screenshotUrlList = emptyList()))

        assertEquals(emptyList<String>(), mapped.screenshotUrlList)
    }

    @Test
    fun unknownCategoryFallsBackToApp() {
        assertEquals(Category.APP, mapper.toDomain(dto.copy(category = "new_category")).category)
    }

    @Test
    fun categoryIsCaseInsensitive() {
        assertEquals(Category.GAME, mapper.toDomain(dto.copy(category = "GaMe")).category)
    }

    @Test
    fun everyCategorySurvivesDtoRoundTrip() {
        Category.entries.forEach { category ->
            val mapped = mapper.toDomain(mapper.toDto(domain.copy(category = category)))

            assertEquals(category, mapped.category)
        }
    }
}
