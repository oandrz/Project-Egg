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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.contentDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import starbright.com.projectegg.compose.ui.screens.detail.RecipeDetailUiState

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
            
            Button(
                onClick = { currentScreen = Screen.RECIPE_DETAIL }
            ) {
                Text("Recipe Detail")
            }
        }
        
        // Test buttons to demonstrate the fix
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    // Simulate successful search
                    searchState = searchState.copy(
                        isSearching = false,
                        currentQuery = "chicken",
                        searchResults = MockDataProvider.getRecipes().filter { 
                            it.title.contains("chicken", ignoreCase = true) 
                        },
                        error = null
                    )
                }
            ) {
                Text("Test Success")
            }
            
            Button(
                onClick = {
                    // Simulate failed search
                    searchState = searchState.copy(
                        isSearching = false,
                        currentQuery = "nonexistent",
                        searchResults = emptyList(),
                        error = "No recipes found for 'nonexistent'"
                    )
                }
            ) {
                Text("Test Error")
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
                        currentScreen = Screen.RECIPE_DETAIL
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
            
            Screen.RECIPE_DETAIL -> {
                var recipeDetailState by remember { 
                    mutableStateOf(RecipeDetailUiState(
                        isLoading = false,
                        recipe = MockDataProvider.getRecipeDetailById(1),
                        isFavorite = false,
                        selectedIngredients = emptySet(),
                        error = null
                    ))
                }
                
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Test buttons for recipe detail
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                // Simulate loading recipe detail
                                recipeDetailState = recipeDetailState.copy(
                                    isLoading = false,
                                    recipe = MockDataProvider.getRecipeDetailById(1),
                                    error = null
                                )
                            }
                        ) {
                            Text("Load Recipe")
                        }
                        
                        Button(
                            onClick = {
                                // Simulate loading error
                                recipeDetailState = recipeDetailState.copy(
                                    isLoading = false,
                                    recipe = null,
                                    error = "Failed to load recipe details"
                                )
                            }
                        ) {
                            Text("Test Error")
                        }
                        
                        Button(
                            onClick = {
                                currentScreen = Screen.SEARCH
                            }
                        ) {
                            Text("Back to Search")
                        }
                    }
                    
                    // Recipe detail content
                    when {
                        recipeDetailState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        
                        recipeDetailState.recipe != null -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = recipeDetailState.recipe!!.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Text(
                                    text = "Cooking Time: ${recipeDetailState.recipe!!.cookingMinutes} minutes",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                
                                Text(
                                    text = "Servings: ${recipeDetailState.recipe!!.servingCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                Text(
                                    text = "Ingredients:",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                recipeDetailState.recipe!!.ingredients?.forEach { ingredient ->
                                    Text(
                                        text = "• ${ingredient.name ?: ingredient.toString()}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
                                    )
                                }
                            }
                        }
                        
                        recipeDetailState.error != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = recipeDetailState.error!!,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(32.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            else -> {
                Text("Screen not implemented")
            }
        }
    }
}

enum class Screen {
    SEARCH,
    RECIPE_DETAIL
} 