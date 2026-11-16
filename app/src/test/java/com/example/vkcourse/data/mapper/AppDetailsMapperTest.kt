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

    @Test
    fun mapsEveryFieldAndConvertsCategoryToDomainEnum() {
        val expected = AppDetails(
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

        assertEquals(expected, mapper.toDomain(dto))
    }

    @Test
    fun missingScreenshotsBecomeEmptyDomainList() {
        val app = mapper.toDomain(dto.copy(screenshotUrlList = null))

        assertEquals(emptyList<String>(), app.screenshotUrlList)
    }

    @Test
    fun unknownCategoryFallsBackToApp() {
        assertEquals(Category.APP, mapper.toDomain(dto.copy(category = "new_category")).category)
    }

    @Test
    fun categoryIsCaseInsensitive() {
        assertEquals(Category.GAME, mapper.toDomain(dto.copy(category = "GaMe")).category)
    }
}
