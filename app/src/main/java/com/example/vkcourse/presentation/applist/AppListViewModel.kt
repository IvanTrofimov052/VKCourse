package com.example.vkcourse.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.repository.AppRepository
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
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)

sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    // Hilt подставляет AppRepositoryImpl, зарегистрированный в AppModule.
    private val repository: AppRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    // replay = 0: новый экран не получает уже показанные сообщения.
    private val _events = MutableSharedFlow<AppListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadApps()
    }

    fun loadApps() {
        // Повторное нажатие «Повторить» не создаёт параллельный запрос.
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            try {
                val apps = repository.getApps()
                _uiState.value = AppListUiState(apps = apps, isLoading = false)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                _uiState.update { state -> state.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun onToolbarClick() {
        _events.tryEmit(AppListEvent.ShowToolbarSnackbar)
    }
}
