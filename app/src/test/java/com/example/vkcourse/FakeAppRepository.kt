package com.example.vkcourse

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.Category
import com.example.vkcourse.domain.repository.AppRepository

object TestApps {
    val featured = AppDetails(
        id = "featured",
        name = "Featured app",
        developer = "Developer",
        category = Category.GAME,
        ageRating = 12,
        size = 42.5f,
        iconUrl = "https://example.com/icon.png",
        screenshotUrlList = listOf("https://example.com/screenshot.png"),
        description = "Description",
    )
    val showcase = featured.copy(id = "showcase", name = "Showcase app")
    val apps = listOf(featured, showcase)
}

class FakeAppRepository(
    private val apps: List<AppDetails> = emptyList(),
    private val showcaseApps: List<AppDetails> = emptyList(),
) : AppRepository {
    var getAppsCalls = 0
        private set
    val requestedAppIds = mutableListOf<String>()
    var getShowcaseAppsCalls = 0
        private set

    override fun getApps(): List<AppDetails> {
        getAppsCalls++
        return apps
    }

    override fun getApp(id: String): AppDetails? {
        requestedAppIds.add(id)
        return apps.find { it.id == id }
    }

    override fun getShowcaseApps(): List<AppDetails> {
        getShowcaseAppsCalls++
        return showcaseApps
    }
}
