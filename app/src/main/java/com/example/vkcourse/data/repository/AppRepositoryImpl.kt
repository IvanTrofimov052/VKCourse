package com.example.vkcourse.data.repository

import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.mapper.CatalogAppMapper
import com.example.vkcourse.data.network.AppApi
import com.example.vkcourse.data.source.RemoteAppDataSource
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.model.RatingsLoadResult
import com.example.vkcourse.domain.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val dataSource: RemoteAppDataSource,
    private val appApi: AppApi,
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

    override suspend fun loadRatingsSequentially(ids: List<String>): RatingsLoadResult {
        val ratings = linkedMapOf<String, Float>()
        val elapsed = measureTimeMillis {
            // Каждый следующий запрос начинается только после завершения предыдущего.
            for (id in ids) {
                ratings[id] = appApi.getRating(id)
            }
        }
        return RatingsLoadResult(ratings = ratings, elapsedMillis = elapsed)
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): RatingsLoadResult =
        coroutineScope {
            var ratings = emptyMap<String, Float>()
            val elapsed = measureTimeMillis {
                // Все async стартуют сразу, awaitAll ждёт завершения последнего.
                ratings = ids
                    .map { id -> async { id to appApi.getRating(id) } }
                    .awaitAll()
                    .toMap()
            }
            RatingsLoadResult(ratings = ratings, elapsedMillis = elapsed)
        }

    private companion object {
        const val SHOWCASE_SIZE = 9
    }
}
