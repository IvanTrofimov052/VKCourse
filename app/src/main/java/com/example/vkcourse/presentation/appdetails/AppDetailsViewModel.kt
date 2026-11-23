package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkcourse.domain.usecase.GetAppDetailsUseCase
import com.example.vkcourse.domain.usecase.GetShowcaseAppsUseCase
import com.example.vkcourse.presentation.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAppDetails: GetAppDetailsUseCase,
    private val getShowcaseApps: GetShowcaseAppsUseCase,
) : ViewModel() {
    private val appId: String = requireNotNull(savedStateHandle[AppDestinations.APP_ID]) {
        "Не передан аргумент навигации ${AppDestinations.APP_ID}"
    }

    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val uiState = _state.asStateFlow()

    private var loadJob: Job? = null
    private var installJob: Job? = null

    init {
        load()
    }

    fun load() {
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _state.value = AppDetailsState.Loading

            try {
                val app = getAppDetails(appId)
                // Витрина не критична для карточки: при её ошибке показываем пустой блок.
                val showcase = try {
                    getShowcaseApps()
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (error: Exception) {
                    emptyList()
                }

                _state.value = if (app == null) {
                    AppDetailsState.NotFound
                } else {
                    AppDetailsState.Content(app = app, showcaseApps = showcase)
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                _state.value = AppDetailsState.Error
            }
        }
    }

    fun onInstallClick() {
        val current = _state.value
        if (current !is AppDetailsState.Content) return

        if (current.installState is InstallState.Installing) {
            // Отмена: корутина установки получает CancellationException на ближайшей
            // точке приостановки (delay) и не доходит до InstallState.Installed.
            installJob?.cancel()
            installJob = null
            updateInstallState(InstallState.NotInstalled)
            return
        }

        if (current.installState is InstallState.Installed) return
        if (installJob?.isActive == true) return

        installJob = viewModelScope.launch {
            try {
                emulateInstall()
            } catch (cancellation: CancellationException) {
                // Если установку отменили не нажатием кнопки (например, экран закрыли),
                // возвращаем кнопку в исходное состояние.
                updateInstallState(InstallState.NotInstalled)
                throw cancellation
            }
        }
    }

    private suspend fun emulateInstall() {
        updateInstallState(InstallState.Installing(progress = 0))

        for (progress in INSTALL_PROGRESS_STEP..MAX_PROGRESS step INSTALL_PROGRESS_STEP) {
            delay(INSTALL_TICK_MILLIS)
            updateInstallState(InstallState.Installing(progress = progress))
        }

        updateInstallState(InstallState.Installed)
    }

    private fun updateInstallState(installState: InstallState) {
        _state.update { state ->
            if (state is AppDetailsState.Content) {
                state.copy(installState = installState)
            } else {
                state
            }
        }
    }

    private companion object {
        const val MAX_PROGRESS = 100
        const val INSTALL_PROGRESS_STEP = 5
        const val INSTALL_TICK_MILLIS = 150L
    }
}
