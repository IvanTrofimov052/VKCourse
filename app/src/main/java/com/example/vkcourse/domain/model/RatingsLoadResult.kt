package com.example.vkcourse.domain.model

/**
 * Результат загрузки рейтингов: сами оценки по id приложений и затраченное
 * на загрузку время. Время измеряется в репозитории, чтобы сравнение
 * последовательного и параллельного способов было одинаковым для любого экрана.
 */
data class RatingsLoadResult(
    val ratings: Map<String, Float>,
    val elapsedMillis: Long,
)
