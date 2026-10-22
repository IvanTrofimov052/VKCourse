package com.example.vkcourse.ui.appdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.vkcourse.R
import com.example.vkcourse.model.AppDetails
import com.example.vkcourse.ui.components.AppListItem

private const val PAGE_COUNT = 3
private const val APPS_PER_PAGE = 3

@Composable
fun Showcase(
    apps: List<AppDetails>,
    onAppClick: (AppDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(apps.size == PAGE_COUNT * APPS_PER_PAGE) {
        "В витрине должно быть ровно 9 приложений"
    }
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

    Column(modifier) {
        Text(
            text = stringResource(R.string.showcase_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 4.dp),
            pageSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth().testTag("showcase_pager"),
        ) { page ->
            Column(Modifier.fillMaxWidth().testTag("showcase_page_$page")) {
                val firstIndex = page * APPS_PER_PAGE
                apps.subList(firstIndex, firstIndex + APPS_PER_PAGE).forEach { app ->
                    AppListItem(
                        app = app,
                        onClick = { onAppClick(app) },
                        modifier = Modifier.testTag("showcase_app_${app.id}"),
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        val pageDescription = stringResource(
            R.string.showcase_page, pagerState.currentPage + 1, PAGE_COUNT,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = pageDescription },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            repeat(PAGE_COUNT) { page ->
                Box(
                    Modifier.size(8.dp).background(
                        color = if (page == pagerState.currentPage) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = CircleShape,
                    ),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
