package com.example.vkcourse.presentation.applist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.vkcourse.R

@Composable
fun AppListRoute(
    onAppClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val toolbarMessage = stringResource(R.string.apps_toolbar_message)

    LaunchedEffect(viewModel, lifecycleOwner, snackbarHostState, toolbarMessage) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    AppListEvent.ShowToolbarSnackbar -> snackbarHostState.showSnackbar(toolbarMessage)
                }
            }
        }
    }

    AppListScreen(
        apps = uiState.apps,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        onAppClick = onAppClick,
        onRetryClick = viewModel::loadApps,
        onToolbarClick = viewModel::onToolbarClick,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
