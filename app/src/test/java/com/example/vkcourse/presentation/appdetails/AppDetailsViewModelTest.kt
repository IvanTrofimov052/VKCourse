package com.example.vkcourse.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vkcourse.FakeAppRepository
import com.example.vkcourse.MainDispatcherRule
import com.example.vkcourse.TestApps
import com.example.vkcourse.domain.usecase.GetAppDetailsUseCase
import com.example.vkcourse.domain.usecase.GetShowcaseAppsUseCase
import com.example.vkcourse.presentation.navigation.AppDestinations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
                    viewModel(
                        repository = repository,
                        appId = TestApps.showcase.id,
                    )
                }
            }

            try {
                val original = ViewModelProvider.create(store, factory)[AppDetailsViewModel::class]
                val recreated = ViewModelProvider.create(store, factory)[AppDetailsViewModel::class]

                assertSame(original, recreated)
                val state = recreated.uiState.value as AppDetailsState.Content
                assertEquals(TestApps.showcaseDetails, state.app)
                assertEquals(listOf(TestApps.showcase), state.showcaseApps)
                assertEquals(InstallState.NotInstalled, state.installState)
                assertEquals(listOf(TestApps.showcase.id), repository.requestedAppIds)
                assertEquals(1, repository.getShowcaseAppsCalls)
                assertEquals(0, repository.getAppsCalls)
            } finally {
                store.clear()
            }
        }

    @Test
    fun unknownAppIsRepresentedByNotFoundWithoutCrashing() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = viewModel(
                repository = FakeAppRepository(),
                appId = "missing",
            )

            assertTrue(viewModel.uiState.value is AppDetailsState.NotFound)
        }

    @Test
    fun installProgressesAndCancelReturnsToNotInstalled() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = installedAppViewModel()

            viewModel.onInstallClick()
            assertEquals(InstallState.Installing(0), viewModel.content().installState)

            advanceTimeBy(151)
            assertEquals(InstallState.Installing(5), viewModel.content().installState)

            // Повторное нажатие отменяет job и возвращает кнопку в исходное состояние.
            viewModel.onInstallClick()
            assertEquals(InstallState.NotInstalled, viewModel.content().installState)

            // Отменённая установка не должна сама дорасти до Installed.
            advanceUntilIdle()
            assertEquals(InstallState.NotInstalled, viewModel.content().installState)
        }

    @Test
    fun installReachesInstalledStateAfterProgressCompletes() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = installedAppViewModel()

            viewModel.onInstallClick()
            advanceUntilIdle()

            assertEquals(InstallState.Installed, viewModel.content().installState)
        }

    private fun installedAppViewModel() = viewModel(
        repository = FakeAppRepository(details = listOf(TestApps.featuredDetails)),
        appId = TestApps.featured.id,
    )

    private fun viewModel(repository: FakeAppRepository, appId: String) = AppDetailsViewModel(
        savedStateHandle = savedStateHandle(appId),
        getAppDetails = GetAppDetailsUseCase(repository),
        getShowcaseApps = GetShowcaseAppsUseCase(repository),
    )

    private fun AppDetailsViewModel.content() = uiState.value as AppDetailsState.Content

    private fun savedStateHandle(appId: String) =
        SavedStateHandle(mapOf(AppDestinations.APP_ID to appId))
}
