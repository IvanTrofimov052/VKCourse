package com.example.vkcourse.presentation.applist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkcourse.R
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.domain.repository.AppRepository

@Composable
fun AppListRoute(
    repository: AppRepository,
    onAppClick: (AppDetails) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppListViewModel = viewModel(factory = AppListViewModel.factory(repository)),
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
        onAppClick = onAppClick,
        onToolbarClick = viewModel::onToolbarClick,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
