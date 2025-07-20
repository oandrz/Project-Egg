package starbright.com.projectegg.compose.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.navigation.Screen
import starbright.com.projectegg.compose.ui.components.ChefnutBottomNavigation

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Show bottom navigation only for main screens
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Favorites.route
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ChefnutBottomNavigation(
                    currentRoute = currentRoute ?: Screen.Home.route,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.startDestinationId) {
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                // TODO: HomeScreen with ViewModel
                // HomeScreen(
                //     uiState = viewModel.uiState.collectAsState().value,
                //     onSearchClick = { navController.navigate(Screen.Search.route) },
                //     onRecipeClick = { recipeId ->
                //         navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                //     },
                //     onLoadMore = { viewModel.loadMoreRecipes() },
                //     onRefresh = { viewModel.refresh() }
                // )
            }
            
            composable(Screen.Favorites.route) {
                // TODO: Get FavoritesViewModel from DI or ViewModelProvider
                // FavoritesScreen(
                //     uiState = viewModel.uiState.collectAsState().value,
                //     onRecipeClick = { recipeId ->
                //         navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                //     },
                //     onRemoveFavorite = { recipeId ->
                //         viewModel.removeFavorite(recipeId)
                //     },
                //     onRefresh = { viewModel.refresh() },
                //     onDiscoverRecipes = {
                //         navController.navigate(Screen.Home.route) {
                //             popUpTo(navController.graph.startDestinationId)
                //             launchSingleTop = true
                //         }
                //     }
                // )
            }
            
            composable(Screen.Search.route) {
                // TODO: Get SearchViewModel from DI or ViewModelProvider
                // SearchScreen(
                //     uiState = viewModel.uiState.collectAsState().value,
                //     onSearchQueryChanged = { query ->
                //         viewModel.searchRecipes(query)
                //     },
                //     onBackClick = { navController.popBackStack() },
                //     onRecipeClick = { recipeId ->
                //         navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                //     },
                //     onSearchHistoryClick = { query ->
                //         viewModel.onSearchHistoryClicked(query)
                //     },
                //     onDeleteSearchHistory = { historyId ->
                //         viewModel.deleteSearchHistory(historyId)
                //     }
                // )
            }
            
            composable(
                route = Screen.RecipeDetail.route,
                arguments = listOf(
                    navArgument("recipeId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                // TODO: Get RecipeDetailViewModel from DI or ViewModelProvider
                // RecipeDetailScreen(
                //     uiState = viewModel.uiState.collectAsState().value,
                //     onBackClick = { navController.popBackStack() },
                //     onFavoriteClick = { viewModel.toggleFavorite() },
                //     onIngredientChecked = { id, checked ->
                //         viewModel.onIngredientChecked(id, checked)
                //     },
                //     onAddToCartClick = { viewModel.onAddToCartClick() },
                //     onSourceClick = { viewModel.onSourceClick() }
                // )
            }
        }
    }
} 