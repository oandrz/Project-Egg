package starbright.com.projectegg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import starbright.com.projectegg.R
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.navigation.ChefnutNavHost
import starbright.com.projectegg.compose.navigation.Screen
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme

/**
 * Main activity for the Chefnut app using Jetpack Compose
 * This serves as the entry point and handles navigation
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ChefnutTheme {
                ChefnutApp()
            }
        }
    }
}

@Composable
fun ChefnutApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Determine if we should show bottom navigation
    val shouldShowBottomBar = when (currentDestination?.route) {
        Screen.Home.route, Screen.Favorites.route -> true
        else -> false
    }
    
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    val items = listOf(BottomNavItem.HOME, BottomNavItem.FAVORITES)
                    
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = when (item) {
                                            BottomNavItem.HOME -> R.drawable.ic_home_red
                                            BottomNavItem.FAVORITES -> R.drawable.ic_bookmark
                                        }
                                    ),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            ChefnutNavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            )
        }
    }
} 