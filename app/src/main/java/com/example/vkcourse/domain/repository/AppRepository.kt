package com.example.vkcourse.domain.repository

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp

interface AppRepository {
    /** Каталог приложений: краткие карточки из GET /catalog. */
    suspend fun getApps(): List<CatalogApp>

    /** Подробная информация о приложении: GET /{id}. null — приложение не найдено. */
    suspend fun getApp(id: String): AppDetails?

    /** Девять приложений для витрины на экране подробностей. */
    suspend fun getShowcaseApps(): List<CatalogApp>
}
