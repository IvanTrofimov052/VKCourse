package com.example.vkcourse.ui.applist

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.data.AppCatalog
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AppListViewModelTest {
    @Test
    fun retainedViewModelLoadsCatalogOnlyOnce() {
        var loadCount = 0
        val apps = AppCatalog.apps
        val store = ViewModelStore()
        val factory = viewModelFactory {
            initializer {
                AppListViewModel(loadApps = {
                    loadCount++
                    apps
                })
            }
        }

        try {
            val original = ViewModelProvider.create(store, factory)[AppListViewModel::class]
            // После пересоздания экрана новый provider использует сохранённый ViewModelStore.
            val recreated = ViewModelProvider.create(store, factory)[AppListViewModel::class]

            assertSame(original, recreated)
            assertSame(apps, recreated.uiState.value.apps)
            assertEquals(1, loadCount)
        } finally {
            store.clear()
        }
    }

    @Test
    fun toolbarEventsAreNotReplayedToRecreatedScreen() = runBlocking {
        val viewModel = AppListViewModel(loadApps = { emptyList() })
        val originalEvents = mutableListOf<AppListEvent>()
        val originalCollector = launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.events.collect { originalEvents.add(it) }
        }

        viewModel.onToolbarClick()
        yield()
        assertEquals(listOf(AppListEvent.ShowToolbarSnackbar), originalEvents)
        originalCollector.cancelAndJoin()

        // События без видимого экрана тоже не должны показываться при новой подписке.
        viewModel.onToolbarClick()
        val recreatedEvents = mutableListOf<AppListEvent>()
        val recreatedCollector = launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.events.collect { recreatedEvents.add(it) }
        }
        try {
            yield()
            assertEquals(emptyList<AppListEvent>(), recreatedEvents)

            viewModel.onToolbarClick()
            yield()
            assertEquals(listOf(AppListEvent.ShowToolbarSnackbar), recreatedEvents)
        } finally {
            recreatedCollector.cancelAndJoin()
        }
    }
}
