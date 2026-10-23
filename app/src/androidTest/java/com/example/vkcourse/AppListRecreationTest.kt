package com.example.vkcourse

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vkcourse.data.AppCatalog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppListRecreationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun catalogAndScrollPositionSurviveActivityRecreation() {
        val lastAppTag = "catalog_app_${AppCatalog.apps.last().id}"
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
