package com.example.vkcourse.data.mapper

import com.example.vkcourse.data.dto.CatalogAppDto
import com.example.vkcourse.domain.model.CatalogApp
import javax.inject.Inject

class CatalogAppMapper @Inject constructor() {
    fun toDomain(dto: CatalogAppDto): CatalogApp = CatalogApp(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = CategoryMapper.toDomain(dto.category),
        iconUrl = dto.iconUrl,
    )

    fun toDto(domain: CatalogApp): CatalogAppDto = CatalogAppDto(
        id = domain.id,
        name = domain.name,
        description = domain.description,
        category = CategoryMapper.toApi(domain.category),
        iconUrl = domain.iconUrl,
    )
}
