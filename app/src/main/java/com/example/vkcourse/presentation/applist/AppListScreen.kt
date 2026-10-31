package com.example.vkcourse.presentation.applist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vkcourse.R
import com.example.vkcourse.domain.model.AppDetails
import com.example.vkcourse.presentation.components.AppListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    apps: List<AppDetails>,
    onAppClick: (AppDetails) -> Unit,
    onToolbarClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.testTag("app_list_screen"),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.apps_title)) },
                modifier = Modifier.testTag("app_list_toolbar").clickable(onClick = onToolbarClick),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding).testTag("app_list"),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
        ) {
            items(items = apps, key = { it.id }) { app ->
                AppListItem(
                    app = app,
                    onClick = { onAppClick(app) },
                    modifier = Modifier.testTag("catalog_app_${app.id}"),
                )
            }
        }
    }
}
