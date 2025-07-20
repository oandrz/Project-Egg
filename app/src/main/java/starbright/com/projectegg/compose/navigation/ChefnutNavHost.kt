package starbright.com.projectegg.compose.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import starbright.com.projectegg.compose.MockDataProvider
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailScreen
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailUiState
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesScreen
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesUiState
import starbright.com.projectegg.compose.ui.screens.home.HomeScreen
import starbright.com.projectegg.compose.ui.screens.home.HomeUiState
import starbright.com.projectegg.compose.ui.screens.search.SearchScreen
import starbright.com.projectegg.compose.ui.screens.search.SearchUiState
import starbright.com.projectegg.compose.ui.screens.splash.SplashScreen

@Composable
fun ChefnutNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    val context = LocalContext.current
    
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
            HomeScreen(
                uiState = HomeUiState(
                    isLoading = false,
                    recipes = MockDataProvider.getRecipes(),
                    error = null,
                    isLoadingMore = false
                ),
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onLoadMore = {
                    Toast.makeText(context, "Load more recipes", Toast.LENGTH_SHORT).show()
                },
                onRefresh = {
                    Toast.makeText(context, "Refreshing recipes", Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        composable(Screen.Search.route) {
            var searchState by remember { 
                mutableStateOf(SearchUiState(
                    searchHistory = MockDataProvider.getSearchHistory()
                ))
            }
            
            SearchScreen(
                uiState = searchState,
                onSearchQueryChanged = { query ->
                    searchState = if (query.isNotEmpty()) {
                        searchState.copy(
                            isSearching = false,
                            currentQuery = query,
                            searchResults = MockDataProvider.getRecipes().filter { recipe ->
                                recipe.title.contains(query, ignoreCase = true)
                            }
                        )
                    } else {
                        searchState.copy(
                            isSearching = false,
                            currentQuery = "",
                            searchResults = emptyList()
                        )
                    }
                },
                onBackClick = { navController.popBackStack() },
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onSearchHistoryClick = { query ->
                    searchState = searchState.copy(
                        currentQuery = query,
                        searchResults = MockDataProvider.getRecipes().filter { recipe ->
                            recipe.title.contains(query, ignoreCase = true)
                        }
                    )
                },
                onDeleteSearchHistory = { id ->
                    searchState = searchState.copy(
                        searchHistory = searchState.searchHistory.filter { it.id != id }
                    )
                }
            )
        }
        
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                uiState = FavoritesUiState(
                    isLoading = false,
                    favorites = MockDataProvider.getFavorites(),
                    error = null
                ),
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onRemoveFavorite = { recipeId ->
                    Toast.makeText(context, "Remove favorite: $recipeId", Toast.LENGTH_SHORT).show()
                },
                onRefresh = {
                    Toast.makeText(context, "Refreshing favorites", Toast.LENGTH_SHORT).show()
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
            var detailState by remember {
                mutableStateOf(RecipeDetailUiState(
                    isLoading = false,
                    recipe = MockDataProvider.getRecipeDetail(),
                    isFavorite = false,
                    selectedIngredients = emptySet(),
                    error = null
                ))
            }
            
            RecipeDetailScreen(
                uiState = detailState,
                onBackClick = { navController.popBackStack() },
                onFavoriteClick = {
                    detailState = detailState.copy(isFavorite = !detailState.isFavorite)
                    Toast.makeText(
                        context, 
                        if (detailState.isFavorite) "Added to favorites" else "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onIngredientChecked = { id, checked ->
                    detailState = detailState.copy(
                        selectedIngredients = if (checked) {
                            detailState.selectedIngredients + id
                        } else {
                            detailState.selectedIngredients - id
                        }
                    )
                },
                onAddToCartClick = {
                    Toast.makeText(
                        context,
                        "Add ${detailState.selectedIngredients.size} ingredients to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSourceClick = {
                    Toast.makeText(context, "Open source URL", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
} 