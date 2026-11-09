package com.example.vkcourse.data.repository

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.data.dto.CatalogAppDto
import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.mapper.CatalogAppMapper
import com.example.vkcourse.data.network.CatalogApi
import com.example.vkcourse.data.source.RemoteAppDataSource
import com.example.vkcourse.domain.model.Category
import com.example.vkcourse.domain.repository.AppRepository
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AppRepositoryImplTest {
    @Test
    fun returnsCatalogAsDomainModelsInOriginalOrder() = runTest {
        val api = FakeCatalogApi(
            catalog = listOf(
                catalogDto(index = 0, category = "Игры"),
                catalogDto(index = 1, category = "Производительность"),
            ),
        )
        val apps = repository(api).getApps()

        assertEquals(2, apps.size)
        assertEquals("id-0", apps.first().id)
        assertEquals("App 0", apps.first().name)
        assertEquals("Description 0", apps.first().description)
        assertEquals(Category.GAME, apps.first().category)
        assertEquals(Category.PRODUCTIVITY, apps[1].category)
    }

    @Test
    fun catalogIsRequestedOnlyOnce() = runTest {
        val api = FakeCatalogApi(catalog = (0..11).map { catalogDto(it) })
        val repository = repository(api)

        repository.getApps()
        repository.getApps()
        repository.getShowcaseApps()

        assertEquals(1, api.catalogRequests)
    }

    @Test
    fun showcaseContainsNineAppsAfterFeaturedOne() = runTest {
        val api = FakeCatalogApi(catalog = (0..11).map { catalogDto(it) })
        val showcase = repository(api).getShowcaseApps()

        assertEquals(9, showcase.size)
        assertEquals("id-1", showcase.first().id)
        assertEquals("id-9", showcase.last().id)
    }

    @Test
    fun appDetailsIsRequestedFromRootEndpoint() = runTest {
        val api = FakeCatalogApi(
            catalog = emptyList(),
            rootDetails = mapOf("id-0" to detailsDto("id-0")),
        )

        val app = repository(api).getApp("id-0")

        assertEquals("Detailed id-0", app?.name)
        assertEquals(Category.HEALTH, app?.category)
        assertEquals(listOf("https://example.com/1.png"), app?.screenshotUrlList)
        assertEquals(1, api.rootRequests)
        assertEquals(0, api.catalogPathRequests)
    }

    @Test
    fun appDetailsFallsBackToCatalogPathWhenRootReturns404() = runTest {
        val api = FakeCatalogApi(
            catalog = emptyList(),
            catalogPathDetails = mapOf("id-0" to detailsDto("id-0")),
        )

        val app = repository(api).getApp("id-0")

        assertEquals("Detailed id-0", app?.name)
        assertEquals(1, api.rootRequests)
        assertEquals(1, api.catalogPathRequests)
    }

    @Test
    fun unknownIdReturnsNull() = runTest {
        val api = FakeCatalogApi(catalog = emptyList())

        assertNull(repository(api).getApp("missing"))
    }

    @Test
    fun root404IsRememberedAndNextLookupUsesCatalogPathOnly() = runTest {
        val api = FakeCatalogApi(
            catalog = emptyList(),
            catalogPathDetails = mapOf("id-1" to detailsDto("id-1")),
        )
        val repository = repository(api)

        assertNull(repository.getApp("missing"))
        assertEquals("Detailed id-1", repository.getApp("id-1")?.name)

        assertEquals(1, api.rootRequests)
        assertEquals(2, api.catalogPathRequests)
    }

    private fun repository(api: CatalogApi): AppRepository = AppRepositoryImpl(
        dataSource = RemoteAppDataSource(api),
        catalogMapper = CatalogAppMapper(),
        detailsMapper = AppDetailsMapper(),
    )

    private fun catalogDto(index: Int, category: String = "Игры") = CatalogAppDto(
        id = "id-$index",
        name = "App $index",
        description = "Description $index",
        category = category,
        iconUrl = "https://example.com/$index.png",
    )

    private fun detailsDto(id: String) = AppDetailsDto(
        id = id,
        name = "Detailed $id",
        developer = "Developer",
        category = "Здоровье и фитнес",
        ageRating = 6,
        size = 12.5f,
        iconUrl = "https://example.com/icon.png",
        screenshotUrlList = listOf("https://example.com/1.png"),
        description = "Detailed description",
    )

    private class FakeCatalogApi(
        private val catalog: List<CatalogAppDto>,
        private val rootDetails: Map<String, AppDetailsDto> = emptyMap(),
        private val catalogPathDetails: Map<String, AppDetailsDto> = emptyMap(),
    ) : CatalogApi {
        var catalogRequests = 0
            private set
        var rootRequests = 0
            private set
        var catalogPathRequests = 0
            private set

        override suspend fun getCatalog(): List<CatalogAppDto> {
            catalogRequests++
            return catalog
        }

        override suspend fun getAppDetails(id: String): AppDetailsDto {
            rootRequests++
            return rootDetails[id] ?: throw notFound()
        }

        override suspend fun getCatalogAppDetails(id: String): AppDetailsDto {
            catalogPathRequests++
            return catalogPathDetails[id] ?: throw notFound()
        }

        private fun notFound() = HttpException(
            Response.error<Any>(
                404,
                "".toResponseBody("application/json".toMediaType()),
            ),
        )
    }
}
