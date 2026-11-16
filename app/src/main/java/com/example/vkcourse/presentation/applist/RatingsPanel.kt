package com.example.vkcourse.presentation.applist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vkcourse.R

/**
 * Панель сравнения двух способов загрузки рейтингов. Кнопки запускают
 * последовательный и параллельный варианты по очереди, а результаты
 * обоих замеров остаются на экране для сравнения.
 */
@Composable
fun RatingsPanel(
    appCount: Int,
    isLoading: Boolean,
    benchmark: RatingsBenchmark?,
    onLoadSequentially: () -> Unit,
    onLoadInParallel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("ratings_panel"),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.ratings_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.ratings_hint, appCount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onLoadSequentially,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f).testTag("ratings_sequential_button"),
                ) {
                    Text(stringResource(R.string.ratings_sequential))
                }
                Button(
                    onClick = onLoadInParallel,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f).testTag("ratings_parallel_button"),
                ) {
                    Text(stringResource(R.string.ratings_parallel))
                }
            }

            if (isLoading) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ratings_loading),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            benchmark?.sequentialMillis?.let { sequentialMillis ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.ratings_sequential_result, sequentialMillis),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("ratings_sequential_result"),
                )
            }
            benchmark?.parallelMillis?.let { parallelMillis ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.ratings_parallel_result, parallelMillis),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("ratings_parallel_result"),
                )
            }
            val sequentialMillis = benchmark?.sequentialMillis
            val parallelMillis = benchmark?.parallelMillis
            if (sequentialMillis != null && parallelMillis != null && parallelMillis > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.ratings_speedup,
                        sequentialMillis.toFloat() / parallelMillis,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("ratings_speedup"),
                )
            }
        }
    }
}
