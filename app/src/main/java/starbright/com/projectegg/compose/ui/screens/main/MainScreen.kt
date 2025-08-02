package starbright.com.projectegg.compose.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.navigation.ChefnutNavHost
import starbright.com.projectegg.compose.navigation.Screen
import starbright.com.projectegg.compose.ui.components.ChefnutBottomNavigation
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailScreen
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailViewModel
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesScreen
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesViewModel
import starbright.com.projectegg.compose.ui.screens.home.HomeScreen
import starbright.com.projectegg.compose.ui.screens.home.HomeViewModel
import starbright.com.projectegg.compose.ui.screens.search.SearchScreen
import starbright.com.projectegg.compose.ui.screens.search.SearchViewModel
import starbright.com.projectegg.dagger.factory.DaggerViewModelFactory

@Composable
fun MainScreen(
    viewModelFactory: ViewModelProvider.Factory
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            val currentRoute = currentDestination?.route
            
            // Show bottom bar only for main screens
            val showBottomBar = currentRoute in listOf(
                Screen.Home.route,
                Screen.Favorites.route
            )
            
            if (showBottomBar) {
                ChefnutBottomNavigation(
                    currentRoute = currentRoute ?: Screen.Home.route,
                    onItemClick = { route ->
                        navController.navigate(route) {
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                val uiState by homeViewModel.uiState.collectAsState()
                
                HomeScreen(
                    uiState = uiState,
                    onSearchClick = { navController.navigate(Screen.Search.route) },
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onLoadMore = { homeViewModel.loadMoreRecipes() },
                    onRefresh = { homeViewModel.refresh() }
                )
            }
            
            composable(Screen.Favorites.route) {
                val favoritesViewModel: FavoritesViewModel = viewModel(factory = viewModelFactory)
                val uiState by favoritesViewModel.uiState.collectAsState()
                
                FavoritesScreen(
                    uiState = uiState,
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onRemoveFavorite = { recipeId ->
                        favoritesViewModel.removeFavorite(recipeId)
                    },
                    onRefresh = { favoritesViewModel.refresh() },
                    onDiscoverRecipes = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
            
            composable(Screen.Search.route) {
                val searchViewModel: SearchViewModel = viewModel(factory = viewModelFactory)
                val uiState by searchViewModel.uiState.collectAsState()
                
                SearchScreen(
                    uiState = uiState,
                    onSearchQueryChanged = { query ->
                        searchViewModel.searchRecipes(query)
                    },
                    onBackClick = { navController.popBackStack() },
                    onRecipeClick = { recipeId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                    },
                    onSearchHistoryClick = { query ->
                        searchViewModel.onSearchHistoryClicked(query)
                    },
                    onDeleteSearchHistory = { historyId ->
                        searchViewModel.deleteSearchHistory(historyId)
                    }
                )
            }
            
            composable(
                route = Screen.RecipeDetail.route,
                arguments = listOf(
                    navArgument("recipeId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                val recipeDetailViewModel: RecipeDetailViewModel = viewModel(factory = viewModelFactory)
                val uiState by recipeDetailViewModel.uiState.collectAsState()
                
                RecipeDetailScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onFavoriteClick = { recipeDetailViewModel.toggleFavorite() },
                    onIngredientChecked = { id, checked ->
                        recipeDetailViewModel.onIngredientChecked(id, checked)
                    },
                    onAddToCartClick = { recipeDetailViewModel.onAddToCartClick() },
                    onSourceClick = { recipeDetailViewModel.onSourceClick() }
                )
            }
        }
    }
} 