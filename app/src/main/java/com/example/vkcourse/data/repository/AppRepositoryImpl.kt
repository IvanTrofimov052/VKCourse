package com.example.vkcourse.data.repository

import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.mapper.CatalogAppMapper
import com.example.vkcourse.data.source.RemoteAppDataSource
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.repository.AppRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val dataSource: RemoteAppDataSource,
    private val catalogMapper: CatalogAppMapper,
    private val detailsMapper: AppDetailsMapper,
) : AppRepository {
    private val catalogMutex = Mutex()
    private var catalogCache: List<CatalogApp>? = null

    override suspend fun getApps(): List<CatalogApp> {
        catalogCache?.let { return it }

        // Mutex не даёт отправить два одинаковых запроса каталога одновременно.
        return catalogMutex.withLock {
            catalogCache ?: dataSource.getCatalog()
                .map(catalogMapper::toDomain)
                .also { catalogCache = it }
        }
    }

    override suspend fun getApp(id: String): AppDetails? =
        dataSource.getAppDetails(id)?.let(detailsMapper::toDomain)

    override suspend fun getShowcaseApps(): List<CatalogApp> =
        getApps().drop(1).take(SHOWCASE_SIZE)

    private companion object {
        const val SHOWCASE_SIZE = 9
    }
}
