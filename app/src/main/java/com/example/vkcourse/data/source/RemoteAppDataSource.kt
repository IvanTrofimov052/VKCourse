package com.example.vkcourse.data.source

import com.example.vkcourse.data.dto.AppDetailsDto
import com.example.vkcourse.data.dto.CatalogAppDto
import com.example.vkcourse.data.network.CatalogApi
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * Сетевой источник данных. Ошибки сети и сервера пробрасываются наверх,
 * чтобы ViewModel могла показать состояние ошибки и предложить повтор.
 */
class RemoteAppDataSource @Inject constructor(
    private val api: CatalogApi,
) {
    // После первого 404 на /{id} запоминаем, что в этом развёртывании
    // карточки лежат по /catalog/{id}, и больше не тратим запрос впустую.
    private val rootDetailsAvailable = AtomicBoolean(true)

    suspend fun getCatalog(): List<CatalogAppDto> = api.getCatalog()

    suspend fun getAppDetails(id: String): AppDetailsDto? {
        if (rootDetailsAvailable.get()) {
            try {
                return api.getAppDetails(id)
            } catch (rootPathError: HttpException) {
                if (rootPathError.code() != HTTP_NOT_FOUND) throw rootPathError
                rootDetailsAvailable.set(false)
            }
        }

        return try {
            api.getCatalogAppDetails(id)
        } catch (catalogPathError: HttpException) {
            if (catalogPathError.code() == HTTP_NOT_FOUND) null else throw catalogPathError
        }
    }

    private companion object {
        const val HTTP_NOT_FOUND = 404
    }
}
