package com.example.vkcourse.presentation.applist

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.FakeAppRepository
import com.example.vkcourse.MainDispatcherRule
import com.example.vkcourse.TestApps
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AppListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun retainedViewModelLoadsCatalogOnlyOnce() = runTest(mainDispatcherRule.testDispatcher) {
        val apps = TestApps.apps
        val repository = FakeAppRepository(apps = apps)
        val store = ViewModelStore()
        val factory = viewModelFactory {
            initializer { AppListViewModel(repository = repository) }
        }

        try {
            val original = ViewModelProvider.create(store, factory)[AppListViewModel::class]
            // После пересоздания экрана новый provider использует сохранённый ViewModelStore.
            val recreated = ViewModelProvider.create(store, factory)[AppListViewModel::class]

            assertSame(original, recreated)
            assertSame(apps, recreated.uiState.value.apps)
            assertFalse(recreated.uiState.value.isLoading)
            assertFalse(recreated.uiState.value.isError)
            assertEquals(1, repository.getAppsCalls)
        } finally {
            store.clear()
        }
    }

    @Test
    fun failedCatalogLoadCanBeRetried() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FakeAppRepository(apps = TestApps.apps)
        repository.failCatalogWith(IllegalStateException("no network"))
        val viewModel = AppListViewModel(repository = repository)

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.isError)
        assertTrue(viewModel.uiState.value.apps.isEmpty())

        repository.recoverCatalog()
        viewModel.loadApps()

        assertFalse(viewModel.uiState.value.isError)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(TestApps.apps, viewModel.uiState.value.apps)
        assertEquals(2, repository.getAppsCalls)
    }

    @Test
    fun toolbarEventsAreNotReplayedToRecreatedScreen() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = AppListViewModel(repository = FakeAppRepository())
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
