package com.example.vkcourse.data.mapper

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.domain.model.AppDetails
import javax.inject.Inject

class AppDetailsMapper @Inject constructor() {
    fun toDomain(dto: AppDetailsDto): AppDetails = AppDetails(
        id = dto.id,
        name = dto.name,
        developer = dto.developer,
        category = CategoryMapper.toDomain(dto.category),
        ageRating = dto.ageRating,
        size = dto.size,
        iconUrl = dto.iconUrl,
        screenshotUrlList = dto.screenshotUrlList.orEmpty(),
        description = dto.description,
    )

    fun toDto(domain: AppDetails): AppDetailsDto = AppDetailsDto(
        id = domain.id,
        name = domain.name,
        developer = domain.developer,
        category = CategoryMapper.toApi(domain.category),
        ageRating = domain.ageRating,
        size = domain.size,
        iconUrl = domain.iconUrl,
        screenshotUrlList = domain.screenshotUrlList,
        description = domain.description,
    )
}
