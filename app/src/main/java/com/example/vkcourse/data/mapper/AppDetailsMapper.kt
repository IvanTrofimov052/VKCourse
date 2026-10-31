package com.example.vkcourse.data.mapper

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.Category

class AppDetailsMapper {
    fun toDomain(dto: AppDetailsDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.name,
        developer = dto.developer,
        category = Category.entries.firstOrNull {
            it.name.equals(dto.category, ignoreCase = true)
        } ?: Category.APP,
        ageRating = dto.ageRating,
        size = dto.size,
        iconUrl = dto.iconUrl,
        screenshotUrlList = dto.screenshotUrlList.orEmpty(),
        description = dto.description,
    )
}
