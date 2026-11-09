package com.example.vkcourse

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.model.Category
import com.example.vkcourse.domain.repository.AppRepository

object TestApps {
    val featured = CatalogApp(
        id = "featured",
        name = "Featured app",
        description = "Featured description",
        category = Category.GAME,
        iconUrl = "https://example.com/featured.png",
    )
    val showcase = featured.copy(
        id = "showcase",
        name = "Showcase app",
        description = "Showcase description",
    )
    val apps = listOf(featured, showcase)

    val featuredDetails = AppDetails(
        id = featured.id,
        name = featured.name,
        developer = "Developer",
        category = Category.GAME,
        ageRating = 12,
        size = 42.5f,
        iconUrl = featured.iconUrl,
        screenshotUrlList = listOf("https://example.com/screenshot.png"),
        description = "Detailed description",
    )
    val showcaseDetails = featuredDetails.copy(
        id = showcase.id,
        name = showcase.name,
    )
    val details = listOf(featuredDetails, showcaseDetails)
}

class FakeAppRepository(
    private val apps: List<CatalogApp> = emptyList(),
    private val showcaseApps: List<CatalogApp> = emptyList(),
    private val details: List<AppDetails> = emptyList(),
) : AppRepository {
    var getAppsCalls = 0
        private set
    val requestedAppIds = mutableListOf<String>()
    var getShowcaseAppsCalls = 0
        private set

    // Позволяет проверить состояние ошибки сети.
    var catalogError: Throwable? = null
        private set

    fun failCatalogWith(error: Throwable) {
        catalogError = error
    }

    fun recoverCatalog() {
        catalogError = null
    }

    override suspend fun getApps(): List<CatalogApp> {
        getAppsCalls++
        catalogError?.let { throw it }
        return apps
    }

    override suspend fun getApp(id: String): AppDetails? {
        requestedAppIds.add(id)
        return details.find { it.id == id }
    }

    override suspend fun getShowcaseApps(): List<CatalogApp> {
        getShowcaseAppsCalls++
        return showcaseApps
    }
}
