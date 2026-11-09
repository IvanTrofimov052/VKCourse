package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp
import com.example.vkcourse.domain.repository.AppRepository
import com.example.vkcourse.presentation.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppDetailsUiState(
    val app: AppDetails? = null,
    val showcaseApps: List<CatalogApp> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // Hilt подставляет AppRepositoryImpl, зарегистрированный в AppModule.
    private val repository: AppRepository,
) : ViewModel() {
    private val appId: String = requireNotNull(savedStateHandle[AppDestinations.APP_ID]) {
        "Не передан аргумент навигации ${AppDestinations.APP_ID}"
    }

    private val _uiState = MutableStateFlow(AppDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        if (loadJob?.isActive == true) return

        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            try {
                val app = repository.getApp(appId)
                // Витрина не критична для карточки: при её ошибке показываем пустой блок.
                val showcase = try {
                    repository.getShowcaseApps()
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (error: Exception) {
                    emptyList()
                }
                _uiState.value = AppDetailsUiState(
                    app = app,
                    showcaseApps = showcase,
                    isLoading = false,
                )
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                _uiState.update { state -> state.copy(isLoading = false, isError = true) }
            }
        }
    }
}
