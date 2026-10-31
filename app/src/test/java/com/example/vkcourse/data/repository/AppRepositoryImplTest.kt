package com.example.vkcourse.data.repository

import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.source.LocalAppDataSource
import com.example.vkcourse.domain.model.Category
import com.example.vkcourse.domain.repository.AppRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class AppRepositoryImplTest {
    private val repository: AppRepository = AppRepositoryImpl(
        dataSource = LocalAppDataSource(),
        mapper = AppDetailsMapper(),
    )

    @Test
    fun returnsCatalogAsDomainModelsInOriginalOrder() {
        val apps = repository.getApps()

        assertEquals(10, apps.size)
        assertEquals("fa2e31b8-1234-4cf7-9914-108a170a1b01", apps.first().id)
        assertEquals(Category.GAME, apps.first().category)
        assertEquals("QuickNote", apps[1].name)
        assertEquals(Category.PRODUCTIVITY, apps[1].category)
        assertEquals("DreamSafe", apps.last().name)
        assertEquals(apps.size, apps.map { it.id }.distinct().size)
    }

    @Test
    fun findsEveryAppById() {
        repository.getApps().forEach { app ->
            assertEquals(app, repository.getApp(app.id))
        }
    }

    @Test
    fun unknownIdReturnsNull() {
        assertNull(repository.getApp("missing"))
    }

    @Test
    fun showcaseContainsNineCatalogAppsWithoutFeaturedApp() {
        val apps = repository.getApps()
        val showcase = repository.getShowcaseApps()

        assertEquals(9, showcase.size)
        assertEquals(apps.drop(1), showcase)
        assertFalse(showcase.any { it.id == apps.first().id })
    }
}
