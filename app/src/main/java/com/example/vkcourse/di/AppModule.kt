package com.example.vkcourse.di

import com.example.vkcourse.data.repository.AppRepositoryImpl
import com.example.vkcourse.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    // ViewModel запрашивает интерфейс, а Hilt подставляет AppRepositoryImpl.
    @Binds
    @Singleton
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository
}
