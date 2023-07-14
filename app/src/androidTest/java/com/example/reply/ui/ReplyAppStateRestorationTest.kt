package com.example.reply.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import com.example.reply.R
import com.example.reply.data.local.LocalEmailsDataProvider
import com.example.reply.test.TestCompactWidth
import com.example.reply.test.TestExpandedWidth
import com.example.reply.test.hasStringId
import com.example.reply.test.onNodeWithContentDescriptionForStringId
import com.example.reply.test.onNodeWithStringId
import com.example.reply.test.onNodeWithTagForStringId
import org.junit.Rule
import org.junit.Test

class ReplyAppStateRestorationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_selectedEmailRetained_afterConfigChane() {
        // Setup compact window
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Compact)
        }

        // Given third email is displayed
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].body)
            .assertIsDisplayed()
        // Open detailed page
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].subject)
            .performClick()

        // Verify that it shows the detailed screen for the correct email
        composeTestRule
            .onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertExists()
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].body)
            .assertExists()

        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify that it shows the detailed screen for the correct email
        composeTestRule
            .onNodeWithContentDescriptionForStringId(R.string.navigation_back)
            .assertExists()
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].body)
            .assertExists()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_selectedEmailRetained_afterConfigChange() {
        // Setup expanded window
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyApp(windowSize = WindowWidthSizeClass.Expanded)
        }
        // Given third email is displayed
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].body)
            .assertIsDisplayed()
        // Select third email
        composeTestRule
            .onNodeWithStringId(LocalEmailsDataProvider.allEmails[2].subject)
            .performClick()
        // Verify third emails is displayed on the details screen
        composeTestRule
            .onNodeWithTagForStringId(R.string.details_screen)
            .onChildren()
            .assertAny(
                hasAnyDescendant(
                    composeTestRule.hasStringId(LocalEmailsDataProvider.allEmails[2].body)
                )
            )

        // Simulate a config change
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Verify third emails is still displayed on the details screen
        composeTestRule
            .onNodeWithTagForStringId(R.string.details_screen)
            .onChildren()
            .assertAny(
                hasAnyDescendant(
                    composeTestRule.hasStringId(LocalEmailsDataProvider.allEmails[2].body)
                )
            )

    }
}
