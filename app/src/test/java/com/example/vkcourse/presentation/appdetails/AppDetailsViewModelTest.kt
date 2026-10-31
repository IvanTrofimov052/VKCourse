package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.vkcourse.FakeAppRepository
import com.example.vkcourse.TestApps
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
        val factory = AppDetailsViewModel.factory(TestApps.showcase.id, repository)

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
        val viewModel = AppDetailsViewModel("missing", FakeAppRepository())

        assertNull(viewModel.uiState.value.app)
        assertTrue(viewModel.uiState.value.showcaseApps.isEmpty())
    }
}
