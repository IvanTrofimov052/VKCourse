package com.example.vkcourse.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppListUiState(val apps: List<AppDetails>)

sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

class AppListViewModel(
    repository: AppRepository,
) : ViewModel() {
    // Каталог читается только при создании ViewModel, которая переживает поворот экрана.
    private val _uiState = MutableStateFlow(AppListUiState(apps = repository.getApps()))
    val uiState = _uiState.asStateFlow()

    // replay = 0: новый экран не получает уже показанные сообщения.
    private val _events = MutableSharedFlow<AppListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun onToolbarClick() {
        _events.tryEmit(AppListEvent.ShowToolbarSnackbar)
    }

    companion object {
        fun factory(repository: AppRepository) = viewModelFactory {
            initializer { AppListViewModel(repository) }
        }
    }
}
