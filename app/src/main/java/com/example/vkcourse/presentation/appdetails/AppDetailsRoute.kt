package com.example.vkcourse.presentation.appdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vkcourse.R
import com.example.vkcourse.domain.model.AppDetails

@Composable
fun AppDetailsRoute(
    onBackClick: () -> Unit,
    onShowcaseAppClick: (AppDetails) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val app = uiState.app

    if (app == null) {
        Scaffold(modifier = modifier) { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                TextButton(onClick = onBackClick) { Text(stringResource(R.string.back)) }
                Text(stringResource(R.string.app_not_found))
            }
        }
    } else {
        AppDetailsScreen(
            app = app,
            showcaseApps = uiState.showcaseApps,
            onBackClick = onBackClick,
            onShowcaseAppClick = onShowcaseAppClick,
            modifier = modifier,
        )
    }
}
