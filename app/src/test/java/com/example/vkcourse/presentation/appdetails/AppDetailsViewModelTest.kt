package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.FakeAppRepository
import com.example.vkcourse.TestApps
import com.example.vkcourse.domain.repository.AppRepository
import com.example.vkcourse.presentation.navigation.AppDestinations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AppDetailsViewModelTest {
    @Test
    fun loadsSelectedAppAndShowcaseOnlyOnceForRetainedViewModel() {
        val repository = FakeAppRepository(
            apps = TestApps.apps,
            showcaseApps = listOf(TestApps.showcase),
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
            assertEquals(TestApps.showcase, recreated.uiState.value.app)
            assertEquals(listOf(TestApps.showcase), recreated.uiState.value.showcaseApps)
            assertEquals(listOf(TestApps.showcase.id), repository.requestedAppIds)
            assertEquals(1, repository.getShowcaseAppsCalls)
            assertEquals(0, repository.getAppsCalls)
        } finally {
            store.clear()
        }
    }

    @Test
    fun unknownAppIsRepresentedByNullWithoutCrashing() {
        val viewModel = AppDetailsViewModel(
            savedStateHandle = savedStateHandle("missing"),
            repository = FakeAppRepository(),
        )

        assertNull(viewModel.uiState.value.app)
        assertTrue(viewModel.uiState.value.showcaseApps.isEmpty())
    }

    private fun savedStateHandle(appId: String) =
        SavedStateHandle(mapOf(AppDestinations.APP_ID to appId))
}
