package starbright.com.projectegg.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailScreen
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailUiState
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesScreen
import starbright.com.projectegg.compose.ui.screens.favorites.FavoritesUiState
import starbright.com.projectegg.compose.ui.screens.home.HomeScreen
import starbright.com.projectegg.compose.ui.screens.home.HomeUiState
import starbright.com.projectegg.compose.ui.screens.search.SearchScreen
import starbright.com.projectegg.compose.ui.screens.search.SearchUiState
import starbright.com.projectegg.compose.ui.screens.splash.SplashScreen
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme

/**
 * Test activity to showcase all Compose screens with mock data
 * This allows testing the UI without needing dependency injection
 */
class ComposeScreenTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ChefnutTheme {
                ScreenSelector()
            }
        }
    }
}

@Composable
fun ScreenSelector() {
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }
    val context = LocalContext.current
    
    when (currentScreen) {
        Screen.SPLASH -> {
            SplashScreen(
                onNavigateToHome = {
                    currentScreen = Screen.HOME
                }
            )
        }
        
        Screen.HOME -> {
            HomeScreen(
                uiState = HomeUiState(
                    isLoading = false,
                    recipes = MockDataProvider.getRecipes(),
                    error = null,
                    isLoadingMore = false
                ),
                onSearchClick = {
                    currentScreen = Screen.SEARCH
                },
                onRecipeClick = { recipeId ->
                    Toast.makeText(context, "Recipe clicked: $recipeId", Toast.LENGTH_SHORT).show()
                    currentScreen = Screen.RECIPE_DETAIL
                },
                onLoadMore = {
                    Toast.makeText(context, "Load more triggered", Toast.LENGTH_SHORT).show()
                },
                onRefresh = {
                    Toast.makeText(context, "Refresh triggered", Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        Screen.SEARCH -> {
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
                onBackClick = {
                    currentScreen = Screen.HOME
                },
                onRecipeClick = { recipeId ->
                    Toast.makeText(context, "Recipe clicked: $recipeId", Toast.LENGTH_SHORT).show()
                    currentScreen = Screen.RECIPE_DETAIL
                },
                onSearchHistoryClick = { query ->
                    Toast.makeText(context, "History clicked: $query", Toast.LENGTH_SHORT).show()
                },
                onDeleteSearchHistory = { id ->
                    Toast.makeText(context, "Delete history: $id", Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        Screen.FAVORITES -> {
            FavoritesScreen(
                uiState = FavoritesUiState(
                    isLoading = false,
                    favorites = MockDataProvider.getFavorites(),
                    error = null
                ),
                onRecipeClick = { recipeId ->
                    Toast.makeText(context, "Recipe clicked: $recipeId", Toast.LENGTH_SHORT).show()
                    currentScreen = Screen.RECIPE_DETAIL
                },
                onRemoveFavorite = { recipeId ->
                    Toast.makeText(context, "Remove favorite: $recipeId", Toast.LENGTH_SHORT).show()
                },
                onRefresh = {
                    Toast.makeText(context, "Refresh favorites", Toast.LENGTH_SHORT).show()
                },
                onDiscoverRecipes = {
                    currentScreen = Screen.HOME
                }
            )
        }
        
        Screen.RECIPE_DETAIL -> {
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
                onBackClick = {
                    currentScreen = Screen.HOME
                },
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
        
        Screen.MENU -> {
            Scaffold { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Compose Screen Selector",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Button(
                        onClick = { currentScreen = Screen.SPLASH },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("Splash Screen")
                    }
                    
                    Button(
                        onClick = { currentScreen = Screen.HOME },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("Home Screen")
                    }
                    
                    Button(
                        onClick = { currentScreen = Screen.SEARCH },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("Search Screen")
                    }
                    
                    Button(
                        onClick = { currentScreen = Screen.FAVORITES },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("Favorites Screen")
                    }
                    
                    Button(
                        onClick = { currentScreen = Screen.RECIPE_DETAIL },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text("Recipe Detail Screen")
                    }
                }
            }
        }
    }
}

private enum class Screen {
    SPLASH,
    HOME,
    SEARCH,
    FAVORITES,
    RECIPE_DETAIL,
    MENU
} 