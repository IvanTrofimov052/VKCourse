package com.example.vkcourse.presentation.applist

import androidx.lifecycle.ViewModel
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AppListUiState(val apps: List<AppDetails>)

sealed interface AppListEvent {
    data object ShowToolbarSnackbar : AppListEvent
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    // Hilt подставляет AppRepositoryImpl, зарегистрированный в AppModule.
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
}
