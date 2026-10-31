package com.example.vkcourse.domain.repository

import com.example.vkcourse.domain.model.AppDetails

interface AppRepository {
    fun getApps(): List<AppDetails>

    fun getApp(id: String): AppDetails?

    fun getShowcaseApps(): List<AppDetails>
}
