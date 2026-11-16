package com.example.vkcourse.domain.repository

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.model.RatingsLoadResult

interface AppRepository {
    /** Каталог приложений: краткие карточки из GET /catalog. */
    suspend fun getApps(): List<CatalogApp>

    /** Подробная информация о приложении: GET /{id}. null — приложение не найдено. */
    suspend fun getApp(id: String): AppDetails?

    /** Девять приложений для витрины на экране подробностей. */
    suspend fun getShowcaseApps(): List<CatalogApp>

    /**
     * Последовательно запрашивает рейтинги: каждый следующий запрос стартует
     * только после завершения предыдущего. Возвращает оценки и время загрузки.
     */
    suspend fun loadRatingsSequentially(ids: List<String>): RatingsLoadResult

    /**
     * Параллельно запрашивает рейтинги через async/awaitAll: все запросы
     * стартуют одновременно. Возвращает оценки и время загрузки.
     */
    suspend fun loadRatingsInParallel(ids: List<String>): RatingsLoadResult
}
