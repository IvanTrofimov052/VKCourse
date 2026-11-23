package com.example.vkcourse.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.usecase.GetAppsUseCase
import com.example.vkcourse.domain.usecase.LoadRatingsInParallelUseCase
import com.example.vkcourse.domain.usecase.LoadRatingsSequentiallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppListUiState(
    val apps: List<CatalogApp> = emptyList(),
    val ratings: Map<String, Float> = emptyMap(),
    val isRatingsLoading: Boolean = false,
    val ratingsBenchmark: RatingsBenchmark? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)

/** Замеры времени двух способов загрузки рейтингов. */
data class RatingsBenchmark(
    val sequentialMillis: Long? = null,
    val parallelMillis: Long? = null,
)

sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val getApps: GetAppsUseCase,
    private val loadRatingsSequentially: LoadRatingsSequentiallyUseCase,
    private val loadRatingsInParallel: LoadRatingsInParallelUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    // replay = 0: новый экран не получает уже показанные сообщения.
    private val _events = MutableSharedFlow<AppListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private var loadJob: Job? = null
    private var ratingsJob: Job? = null

    init {
        loadApps()
    }

    fun loadApps() {
        // Повторное нажатие «Повторить» не создаёт параллельный запрос.
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            try {
                val apps = getApps()
                _uiState.value = AppListUiState(apps = apps, isLoading = false)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                _uiState.update { state -> state.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun loadRatingsSequentially() {
        loadRatings(sequential = true)
    }

    fun loadRatingsInParallel() {
        loadRatings(sequential = false)
    }

    private fun loadRatings(sequential: Boolean) {
        if (ratingsJob?.isActive == true) return

        val ids = _uiState.value.apps.map(CatalogApp::id)
        if (ids.isEmpty()) return

        ratingsJob = viewModelScope.launch {
            _uiState.update { it.copy(isRatingsLoading = true) }

            try {
                val result = if (sequential) {
                    loadRatingsSequentially(ids)
                } else {
                    loadRatingsInParallel(ids)
                }

                _uiState.update { state ->
                    val benchmark = state.ratingsBenchmark ?: RatingsBenchmark()
                    state.copy(
                        ratings = result.ratings,
                        isRatingsLoading = false,
                        ratingsBenchmark = if (sequential) {
                            benchmark.copy(sequentialMillis = result.elapsedMillis)
                        } else {
                            benchmark.copy(parallelMillis = result.elapsedMillis)
                        },
                    )
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                _uiState.update { it.copy(isRatingsLoading = false) }
            }
        }
    }

    fun onToolbarClick() {
        _events.tryEmit(AppListEvent.ShowToolbarSnackbar)
    }
}
