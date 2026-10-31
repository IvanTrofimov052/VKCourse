package com.example.vkcourse.presentation.appdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.vkcourse.R
import com.example.vkcourse.domain.model.AppDetails
import kotlinx.coroutines.launch

@Composable
fun AppDetailsScreen(
    app: AppDetails,
    showcaseApps: List<AppDetails>,
    onBackClick: () -> Unit,
    onShowcaseAppClick: (AppDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Как в практике: false — краткое описание, true — полный текст.
    var descriptionCollapsed by rememberSaveable(app.id) { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val underDevelopment = stringResource(R.string.under_development)
    val showUnderDevelopment: () -> Unit = {
        scope.launch { snackbarHostState.showSnackbar(underDevelopment) }
    }

    Scaffold(
        modifier = modifier.testTag("app_details_screen"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        AppDetailsContent(
            appDetails = app,
            descriptionCollapsed = descriptionCollapsed,
            showcaseApps = showcaseApps,
            onBackClick = onBackClick,
            onShareClick = showUnderDevelopment,
            onInstallClick = showUnderDevelopment,
            onReadMoreClick = { descriptionCollapsed = true },
            onDeveloperClick = showUnderDevelopment,
            onShowcaseAppClick = onShowcaseAppClick,
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        )
    }
}
