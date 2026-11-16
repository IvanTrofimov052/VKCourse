package com.example.vkcourse

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
import com.example.vkcourse.di.AppModule
import com.example.vkcourse.domain.repository.AppRepository
import com.example.vkcourse.presentation.appdetails.Showcase
import com.example.vkcourse.presentation.navigation.AppNavHost
import com.example.vkcourse.presentation.theme.VKCourseTheme
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(AppModule::class)
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val repository: AppRepository = AndroidTestData.repository

    private val showcase = AndroidTestData.showcase

    @Before
    fun inject() {
        hiltRule.inject()
    }

    @Test
    fun showcaseHasThreePagesWithThreeAppsAndStopsAtTheLastPage() {
        compose.setContent {
            VKCourseTheme { Showcase(apps = showcase, onAppClick = {}) }
        }

        repeat(3) { page ->
            compose.onNodeWithTag("showcase_page_$page", useUnmergedTree = true)
                .onChildren().assertCountEquals(3)
            showcase.drop(page * 3).take(3).forEach { app ->
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
        val selectedApp = showcase.first()
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
        showcase.forEachIndexed { index, app ->
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
        compose.setContent { VKCourseTheme { AppNavHost() } }
    }

    private fun openFeaturedApp() {
        val featured = AndroidTestData.apps.first()
        compose.onNodeWithTag("catalog_app_${featured.id}")
            .performScrollTo().performClick()
        compose.onNodeWithTag("app_details_screen").assertIsDisplayed()
    }
}
