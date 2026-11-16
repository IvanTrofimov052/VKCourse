package com.example.vkcourse.presentation.appdetails

import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.model.CatalogApp

/** Состояния экрана карточки приложения. */
sealed interface AppDetailsState {
    data object Loading : AppDetailsState

    data object Error : AppDetailsState

    data object NotFound : AppDetailsState

    data class Content(
        val app: AppDetails,
        val showcaseApps: List<CatalogApp>,
        val installState: InstallState = InstallState.NotInstalled,
    ) : AppDetailsState
}

/** Состояние кнопки установки приложения. */
sealed interface InstallState {
    data object NotInstalled : InstallState

    /** progress — от 0 до 100. */
    data class Installing(val progress: Int) : InstallState

    data object Installed : InstallState
}
