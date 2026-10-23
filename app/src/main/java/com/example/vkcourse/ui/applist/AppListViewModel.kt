package com.example.vkcourse.ui.applist

import androidx.lifecycle.ViewModel
import com.example.vkcourse.data.AppCatalog
import com.example.vkcourse.model.AppDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppListUiState(val apps: List<AppDetails>)

sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

class AppListViewModel(
    loadApps: () -> List<AppDetails> = { AppCatalog.apps },
) : ViewModel() {
    // Каталог читается только при создании ViewModel, которая переживает поворот экрана.
    private val _uiState = MutableStateFlow(AppListUiState(apps = loadApps()))
    val uiState = _uiState.asStateFlow()

    // replay = 0: новый экран не получает уже показанные сообщения.
    private val _events = MutableSharedFlow<AppListEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun onToolbarClick() {
        _events.tryEmit(AppListEvent.ShowToolbarSnackbar)
    }
}
