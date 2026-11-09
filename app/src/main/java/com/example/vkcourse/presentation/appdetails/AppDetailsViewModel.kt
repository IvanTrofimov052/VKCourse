package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository
import com.example.vkcourse.presentation.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AppDetailsUiState(
    val app: AppDetails?,
    val showcaseApps: List<AppDetails>,
)

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // Hilt подставляет AppRepositoryImpl, зарегистрированный в AppModule.
    repository: AppRepository,
) : ViewModel() {
    private val appId: String = requireNotNull(savedStateHandle[AppDestinations.APP_ID]) {
        "Не передан аргумент навигации ${AppDestinations.APP_ID}"
    }

    private val _uiState = MutableStateFlow(
        AppDetailsUiState(
            app = repository.getApp(appId),
            showcaseApps = repository.getShowcaseApps(),
        ),
    )
    val uiState = _uiState.asStateFlow()
}
