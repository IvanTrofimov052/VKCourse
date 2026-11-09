package com.example.vkcourse.data.network

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.data.dto.CatalogAppDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit-описание учебного API.
 *
 * Базовый адрес задаётся в BuildConfig.BASE_URL: http://185.103.109.134/
 */
interface CatalogApi {
    @GET("catalog")
    suspend fun getCatalog(): List<CatalogAppDto>

    // http://185.103.109.134/{id}
    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDetailsDto

    /**
     * Резервный путь для развёрнутого сервера: в текущей конфигурации Apache
     * карточка приложения доступна по /catalog/{id}, а не по /{id}.
     */
    @GET("catalog/{id}")
    suspend fun getCatalogAppDetails(@Path("id") id: String): AppDetailsDto
}
