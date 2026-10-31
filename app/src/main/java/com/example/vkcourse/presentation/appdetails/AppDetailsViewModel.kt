package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppDetailsUiState(
    val app: AppDetails?,
    val showcaseApps: List<AppDetails>,
)

class AppDetailsViewModel(
    appId: String,
    repository: AppRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppDetailsUiState(
            app = repository.getApp(appId),
            showcaseApps = repository.getShowcaseApps(),
        ),
    )
    val uiState = _uiState.asStateFlow()

    companion object {
        fun factory(appId: String, repository: AppRepository) = viewModelFactory {
            initializer { AppDetailsViewModel(appId, repository) }
        }
    }
}
