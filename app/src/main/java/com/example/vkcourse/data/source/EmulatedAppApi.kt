package com.example.vkcourse.data.source

import com.example.vkcourse.data.network.AppApi
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Эмуляция сетевого запроса рейтинга: 1,5 секунды задержки и случайная оценка
 * от 3.0 до 5.0. Задержка нужна, чтобы наглядно сравнить последовательную
 * и параллельную загрузку в каталоге.
 */
class EmulatedAppApi @Inject constructor() : AppApi {
    override suspend fun getRating(id: String): Float {
        delay(NETWORK_DELAY_MILLIS)
        return (MIN_RATING_TENTHS..MAX_RATING_TENTHS).random() / 10f
    }

    private companion object {
        const val NETWORK_DELAY_MILLIS = 1_500L
        const val MIN_RATING_TENTHS = 30
        const val MAX_RATING_TENTHS = 50
    }
}
