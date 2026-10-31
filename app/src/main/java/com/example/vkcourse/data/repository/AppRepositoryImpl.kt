package com.example.vkcourse.data.repository

import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.source.LocalAppDataSource
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository

class AppRepositoryImpl(
    private val dataSource: LocalAppDataSource,
    private val mapper: AppDetailsMapper,
) : AppRepository {
    override fun getApps(): List<AppDetails> = dataSource.getApps().map(mapper::toDomain)

    override fun getApp(id: String): AppDetails? = dataSource.getApp(id)?.let(mapper::toDomain)

    override fun getShowcaseApps(): List<AppDetails> =
        dataSource.getShowcaseApps().map(mapper::toDomain)
}
