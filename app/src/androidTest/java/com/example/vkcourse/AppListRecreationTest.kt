package com.example.vkcourse

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vkcourse.di.AppModule
import com.example.vkcourse.domain.repository.AppRepository
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
class AppListRecreationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val repository: AppRepository = AndroidTestData.repository

    private val lastAppTag = "catalog_app_${AndroidTestData.apps.last().id}"

    @Before
    fun inject() {
        hiltRule.inject()
    }

    @Test
    fun catalogAndScrollPositionSurviveActivityRecreation() {
        compose.onNodeWithTag("app_list").performScrollToNode(hasTestTag(lastAppTag))
        compose.onNodeWithTag(lastAppTag).assertIsDisplayed()

        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("app_list_screen").assertIsDisplayed()
        compose.onNodeWithTag(lastAppTag).assertIsDisplayed()
    }

    @Test
    fun snackbarIsNotShownAgainAfterActivityRecreation() {
        val message = compose.activity.getString(R.string.apps_toolbar_message)
        compose.onNodeWithTag("app_list_toolbar").performClick()
        compose.onNodeWithText(message).assertIsDisplayed()

        compose.activityRule.scenario.recreate()

        compose.onNodeWithTag("app_list_screen").assertIsDisplayed()
        compose.onNodeWithText(message).assertDoesNotExist()
        compose.onNodeWithTag("app_list_toolbar").performClick()
        compose.onNodeWithText(message).assertIsDisplayed()
    }
}
