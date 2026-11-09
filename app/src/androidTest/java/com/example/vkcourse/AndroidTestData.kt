package com.example.vkcourse

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.model.Category
import com.example.vkcourse.domain.repository.AppRepository

/** Детерминированные данные для UI-тестов: сеть в инструментальных тестах не нужна. */
object AndroidTestData {
    val apps: List<CatalogApp> = (0 until 10).map { index ->
        CatalogApp(
            id = "id-$index",
            name = "App $index",
            description = "Description $index",
            category = Category.PRODUCTIVITY,
            iconUrl = "https://fastly.picsum.photos/id/${237 + index}/200/200.jpg",
        )
    }
    val showcase: List<CatalogApp> = apps.drop(1).take(9)

    private val details: Map<String, AppDetails> = apps.associate { app ->
        app.id to AppDetails(
            id = app.id,
            name = app.name,
            developer = "Developer ${app.id}",
            category = app.category,
            ageRating = 6,
            size = 12.5f,
            iconUrl = app.iconUrl,
            screenshotUrlList = emptyList(),
            description = "Detailed ${app.description}",
        )
    }

    val repository: AppRepository = object : AppRepository {
        override suspend fun getApps(): List<CatalogApp> = apps

        override suspend fun getApp(id: String): AppDetails? = details[id]

        override suspend fun getShowcaseApps(): List<CatalogApp> = showcase
    }
}
