package com.example.vkcourse.di

import com.example.vkcourse.data.mapper.AppDetailsMapper
import com.example.vkcourse.data.repository.AppRepositoryImpl
import com.example.vkcourse.data.source.LocalAppDataSource
import com.example.vkcourse.domain.repository.AppRepository

// Точка сборки зависимостей: presentation получает только интерфейс из domain.
object AppContainer {
    val appRepository: AppRepository = AppRepositoryImpl(
        dataSource = LocalAppDataSource(),
        mapper = AppDetailsMapper(),
    )
}
