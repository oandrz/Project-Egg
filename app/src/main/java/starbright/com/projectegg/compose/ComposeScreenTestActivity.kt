package starbright.com.projectegg.compose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import starbright.com.projectegg.compose.ui.screens.search.SearchScreen
import starbright.com.projectegg.compose.ui.screens.search.SearchUiState
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.SearchHistory
import java.util.*

/**
 * Test Activity for demonstrating Compose screens
 * This is used for testing individual screens in isolation
 */
class ComposeScreenTestActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ChefnutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreenDemo()
                }
            }
        }
    }
}

@Composable
fun SearchScreenDemo() {
    val context = LocalContext.current
    
    var currentScreen by remember { mutableStateOf(Screen.SEARCH) }
    var searchState by remember { 
        mutableStateOf(SearchUiState(
            searchHistory = MockDataProvider.getSearchHistory()
        ))
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { currentScreen = Screen.SEARCH }
            ) {
                Text("Search Screen")
            }
        }
        
        // Screen content
        when (currentScreen) {
            Screen.SEARCH -> {
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
                        Toast.makeText(context, "Back button clicked", Toast.LENGTH_SHORT).show()
                    },
                    onRecipeClick = { recipeId ->
                        Toast.makeText(context, "Recipe clicked: $recipeId", Toast.LENGTH_SHORT).show()
                    },
                    onSearchHistoryClick = { query ->
                        Toast.makeText(context, "History clicked: $query", Toast.LENGTH_SHORT).show()
                        // Simulate searching with the clicked history item
                        searchState = searchState.copy(
                            currentQuery = query,
                            searchResults = MockDataProvider.getRecipes().filter { recipe ->
                                recipe.title.contains(query, ignoreCase = true)
                            }
                        )
                    },
                    onDeleteSearchHistory = { id ->
                        Toast.makeText(context, "Delete history: $id", Toast.LENGTH_SHORT).show()
                        // Remove the item from search history
                        searchState = searchState.copy(
                            searchHistory = searchState.searchHistory.filter { it.id != id }
                        )
                    }
                )
            }
            
            else -> {
                Text("Screen not implemented")
            }
        }
    }
}

enum class Screen {
    SEARCH
}

object MockDataProvider {
    
    fun getSearchHistory(): List<SearchHistory> {
        return listOf(
            SearchHistory(
                id = 1,
                query = "Chicken pasta recipe",
                createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
            ),
            SearchHistory(
                id = 2,
                query = "Chocolate cake",
                createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
            ),
            SearchHistory(
                id = 3,
                query = "Vegetarian lasagna",
                createdAt = System.currentTimeMillis() - 10800000 // 3 hours ago
            ),
            SearchHistory(
                id = 4,
                query = "Beef stir fry",
                createdAt = System.currentTimeMillis() - 14400000 // 4 hours ago
            ),
            SearchHistory(
                id = 5,
                query = "Salmon with vegetables",
                createdAt = System.currentTimeMillis() - 18000000 // 5 hours ago
            )
        )
    }
    
    fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = 1,
                title = "Chicken Pasta Carbonara",
                image = "https://example.com/chicken-pasta.jpg",
                cookingMinutes = 30,
                servingCount = 4,
                sourceStringUrl = "http://example.com/recipe1",
                sourceName = "Italian Kitchen",
                cuisines = listOf("Italian"),
                dishTypes = listOf("Pasta")
            ),
            Recipe(
                id = 2,
                title = "Chocolate Lava Cake",
                image = "https://example.com/chocolate-cake.jpg",
                cookingMinutes = 25,
                servingCount = 2,
                sourceStringUrl = "http://example.com/recipe2",
                sourceName = "Dessert Corner",
                cuisines = listOf("French"),
                dishTypes = listOf("Dessert")
            ),
            Recipe(
                id = 3,
                title = "Vegetarian Lasagna",
                image = "https://example.com/vegetarian-lasagna.jpg",
                cookingMinutes = 60,
                servingCount = 6,
                sourceStringUrl = "http://example.com/recipe3",
                sourceName = "Healthy Eats",
                cuisines = listOf("Italian"),
                dishTypes = listOf("Pasta", "Vegetarian")
            ),
            Recipe(
                id = 4,
                title = "Beef Stir Fry with Vegetables",
                image = "https://example.com/beef-stir-fry.jpg",
                cookingMinutes = 20,
                servingCount = 4,
                sourceStringUrl = "http://example.com/recipe4",
                sourceName = "Asian Kitchen",
                cuisines = listOf("Chinese"),
                dishTypes = listOf("Stir Fry")
            ),
            Recipe(
                id = 5,
                title = "Grilled Salmon with Roasted Vegetables",
                image = "https://example.com/salmon-vegetables.jpg",
                cookingMinutes = 35,
                servingCount = 2,
                sourceStringUrl = "http://example.com/recipe5",
                sourceName = "Seafood Delights",
                cuisines = listOf("Mediterranean"),
                dishTypes = listOf("Seafood", "Grilled")
            )
        )
    }
} 