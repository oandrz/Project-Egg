package starbright.com.projectegg.compose.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import starbright.com.projectegg.compose.MockDataProvider
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailScreen
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailUiState
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailViewModel
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesScreen
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesUiState
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesViewModel
import starbright.com.projectegg.compose.ui.screens.home.HomeScreen
import starbright.com.projectegg.compose.ui.screens.home.HomeUiState
import starbright.com.projectegg.compose.ui.screens.home.HomeViewModel
import starbright.com.projectegg.compose.ui.screens.search.SearchScreen
import starbright.com.projectegg.compose.ui.screens.search.SearchUiState
import starbright.com.projectegg.compose.ui.screens.search.SearchViewModel
import starbright.com.projectegg.compose.ui.screens.splash.SplashScreen

@Composable
fun ChefnutNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route,
    viewModelFactory: ViewModelProvider.Factory
) {
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
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
                onDeleteSearchHistory = { id ->
                    searchViewModel.deleteSearchHistory(id)
                }
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
                onRefresh = {
                    favoritesViewModel.refresh()
                },
                onDiscoverRecipes = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
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
            val detailViewModel: RecipeDetailViewModel = viewModel(factory = viewModelFactory)
            val uiState by detailViewModel.uiState.collectAsState()
            
            // Load recipe detail when composable is first created
            remember(recipeId) {
                detailViewModel.loadRecipeDetail(recipeId)
                true
            }
            
            RecipeDetailScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onFavoriteClick = {
                    detailViewModel.toggleFavorite()
                },
                onIngredientChecked = { id, checked ->
                    detailViewModel.onIngredientChecked(id, checked)
                },
                onAddToCartClick = {
                    detailViewModel.onAddToCartClick()
                },
                onSourceClick = {
                    detailViewModel.onSourceClick()
                }
            )
        }
    }
} 