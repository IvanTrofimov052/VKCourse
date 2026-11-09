package com.example.vkcourse.presentation.appdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vkcourse.R

@Composable
fun AppDetailsRoute(
    onBackClick: () -> Unit,
    onShowcaseAppClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val app = uiState.app

    when {
        uiState.isLoading -> {
            Scaffold(modifier = modifier) { contentPadding ->
                Column(Modifier.fillMaxSize().padding(contentPadding)) {
                    BackButton(onBackClick)
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        uiState.isError -> {
            Scaffold(modifier = modifier) { contentPadding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                ) {
                    BackButton(onBackClick)
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically,
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.app_details_error),
                            textAlign = TextAlign.Center,
                        )
                        Button(onClick = viewModel::load) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
        }

        app == null -> {
            Scaffold(modifier = modifier) { contentPadding ->
                Column(Modifier.padding(contentPadding)) {
                    BackButton(onBackClick)
                    Text(stringResource(R.string.app_not_found))
                }
            }
        }

        else -> {
            AppDetailsScreen(
                app = app,
                showcaseApps = uiState.showcaseApps,
                onBackClick = onBackClick,
                onShowcaseAppClick = { showcaseApp -> onShowcaseAppClick(showcaseApp.id) },
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun BackButton(onBackClick: () -> Unit) {
    TextButton(onClick = onBackClick) {
        Text(stringResource(R.string.back))
    }
}
