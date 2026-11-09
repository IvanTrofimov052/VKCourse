package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.FakeAppRepository
import com.example.vkcourse.MainDispatcherRule
import com.example.vkcourse.TestApps
import com.example.vkcourse.presentation.navigation.AppDestinations
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AppDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadsSelectedAppAndShowcaseOnlyOnceForRetainedViewModel() =
        runTest(mainDispatcherRule.testDispatcher) {
            val repository = FakeAppRepository(
                apps = TestApps.apps,
                showcaseApps = listOf(TestApps.showcase),
                details = TestApps.details,
            )
            val store = ViewModelStore()
            val factory = viewModelFactory {
                initializer {
                    AppDetailsViewModel(
                        savedStateHandle = savedStateHandle(TestApps.showcase.id),
                        repository = repository,
                    )
                }
            }

            try {
                val original = ViewModelProvider.create(store, factory)[AppDetailsViewModel::class]
                val recreated = ViewModelProvider.create(store, factory)[AppDetailsViewModel::class]

                assertSame(original, recreated)
                assertEquals(TestApps.showcaseDetails, recreated.uiState.value.app)
                assertEquals(listOf(TestApps.showcase), recreated.uiState.value.showcaseApps)
                assertFalse(recreated.uiState.value.isLoading)
                assertFalse(recreated.uiState.value.isError)
                assertEquals(listOf(TestApps.showcase.id), repository.requestedAppIds)
                assertEquals(1, repository.getShowcaseAppsCalls)
                assertEquals(0, repository.getAppsCalls)
            } finally {
                store.clear()
            }
        }

    @Test
    fun unknownAppIsRepresentedByNullWithoutCrashing() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = AppDetailsViewModel(
                savedStateHandle = savedStateHandle("missing"),
                repository = FakeAppRepository(),
            )

            assertNull(viewModel.uiState.value.app)
            assertTrue(viewModel.uiState.value.showcaseApps.isEmpty())
            assertFalse(viewModel.uiState.value.isLoading)
            assertFalse(viewModel.uiState.value.isError)
        }

    private fun savedStateHandle(appId: String) =
        SavedStateHandle(mapOf(AppDestinations.APP_ID to appId))
}
