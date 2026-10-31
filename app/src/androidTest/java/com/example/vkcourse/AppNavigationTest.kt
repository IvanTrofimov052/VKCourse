package com.example.vkcourse

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vkcourse.di.AppContainer
import com.example.vkcourse.presentation.appdetails.Showcase
import com.example.vkcourse.presentation.navigation.AppNavHost
import com.example.vkcourse.presentation.theme.VKCourseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    private val repository = AppContainer.appRepository

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun showcaseHasThreePagesWithThreeAppsAndStopsAtTheLastPage() {
        compose.setContent {
            VKCourseTheme { Showcase(apps = repository.getShowcaseApps(), onAppClick = {}) }
        }

        repeat(3) { page ->
            compose.onNodeWithTag("showcase_page_$page", useUnmergedTree = true)
                .onChildren().assertCountEquals(3)
            repository.getShowcaseApps().drop(page * 3).take(3).forEach { app ->
                compose.onNodeWithText(app.name).assertIsDisplayed()
            }
            compose.onNodeWithContentDescription("Страница ${page + 1} из 3")
                .assertIsDisplayed()
            compose.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft() }
            compose.waitForIdle()
        }
        compose.onNodeWithContentDescription("Страница 3 из 3").assertIsDisplayed()
        repeat(2) {
            compose.onNodeWithTag("showcase_pager").performTouchInput { swipeRight() }
            compose.waitForIdle()
        }
        compose.onNodeWithContentDescription("Страница 1 из 3").assertIsDisplayed()
    }

    @Test
    fun listOpensSelectedAppAndToolbarBackReturnsToList() {
        showApp()
        val selectedApp = repository.getShowcaseApps().first()
        compose.onNodeWithTag("catalog_app_${selectedApp.id}")
            .performScrollTo().performClick()
        compose.onNodeWithTag("app_details_screen").assertIsDisplayed()
        compose.onNode(
            hasText(selectedApp.name) and hasAnyAncestor(hasTestTag("app_details_header")),
        ).assertIsDisplayed()
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithTag("app_list_screen").assertIsDisplayed()
    }

    @Test
    fun everyShowcaseAppReturnsToListWithoutLeavingDetailsInBackStack() {
        showApp()
        repository.getShowcaseApps().forEachIndexed { index, app ->
            openFeaturedApp()
            compose.onNodeWithTag("showcase_pager").performScrollTo()
            repeat(index / 3) {
                compose.onNodeWithTag("showcase_pager").performTouchInput { swipeLeft() }
                compose.waitForIdle()
            }
            compose.onNodeWithTag("showcase_app_${app.id}")
                .performScrollTo().performClick()
            compose.onNodeWithTag("app_list_screen").assertIsDisplayed()
        }

        // После возвратов из витрины Back должен убрать только новую карточку.
        openFeaturedApp()
        pressBack()
        compose.onNodeWithTag("app_list_screen").assertIsDisplayed()
        compose.onNodeWithTag("app_details_screen").assertDoesNotExist()
    }

    private fun showApp() {
        compose.setContent { VKCourseTheme { AppNavHost(repository = repository) } }
    }

    private fun openFeaturedApp() {
        compose.onNodeWithTag("catalog_app_${repository.getApps().first().id}")
            .performScrollTo().performClick()
        compose.onNodeWithTag("app_details_screen").assertIsDisplayed()
    }
}
