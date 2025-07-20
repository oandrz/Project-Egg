package starbright.com.projectegg.compose.ui.screens.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_showsLogo() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                SplashScreen()
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Chefnut Logo")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_showsLoadingIndicator() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                SplashScreen()
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading indicator")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_showsAppName() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                SplashScreen()
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Chefnut")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_showsTagline() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                SplashScreen()
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Discover Your Next Favorite Recipe")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_navigatesToHome_afterDelay() {
        // Given
        var navigatedToHome = false
        composeTestRule.setContent {
            ChefnutTheme {
                SplashScreen(
                    onNavigateToHome = { navigatedToHome = true }
                )
            }
        }

        // When - advance time by 2 seconds
        composeTestRule.mainClock.advanceTimeBy(2000)

        // Then
        assert(navigatedToHome) { "Should navigate to home after delay" }
    }
} 