package starbright.com.projectegg.compose.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ChefnutBottomNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNavigation_showsAllItems() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.HOME.screen.route,
                    onItemClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Home")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Favourite")
            .assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_showsCorrectIcons() {
        // Given
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.HOME.screen.route,
                    onItemClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Home icon")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Favourite icon")
            .assertIsDisplayed()
    }

    @Test
    fun bottomNavigation_highlightsSelectedItem() {
        // Given - Home is selected
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.HOME.screen.route,
                    onItemClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Home")
            .assertIsSelected()
        composeTestRule
            .onNodeWithText("Favourite")
            .assertIsNotSelected()
    }

    @Test
    fun bottomNavigation_switchesSelection_whenDifferentRouteProvided() {
        // Given - Favorites is selected
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.FAVORITES.screen.route,
                    onItemClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Home")
            .assertIsNotSelected()
        composeTestRule
            .onNodeWithText("Favourite")
            .assertIsSelected()
    }

    @Test
    fun bottomNavigation_triggersCallback_whenItemClicked() {
        // Given
        var clickedRoute = ""
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.HOME.screen.route,
                    onItemClick = { route -> clickedRoute = route }
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithText("Favourite")
            .performClick()

        // Then
        assert(clickedRoute == BottomNavItem.FAVORITES.screen.route) {
            "Should call onItemClick with favorites route"
        }
    }

    @Test
    fun bottomNavigation_doesNotTriggerCallback_whenCurrentItemClicked() {
        // Given
        var clickCount = 0
        composeTestRule.setContent {
            ChefnutTheme {
                ChefnutBottomNavigation(
                    currentRoute = BottomNavItem.HOME.screen.route,
                    onItemClick = { clickCount++ }
                )
            }
        }

        // When
        composeTestRule
            .onNodeWithText("Home")
            .performClick()

        // Then
        assert(clickCount == 0) {
            "Should not trigger callback when clicking already selected item"
        }
    }
} 