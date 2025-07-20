package starbright.com.projectegg.compose.ui.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import starbright.com.projectegg.R
import starbright.com.projectegg.compose.ui.components.FavoriteRecipeCard
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.local.FavouriteRecipe

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onRecipeClick: (String) -> Unit,
    onRemoveFavorite: (String) -> Unit,
    onRefresh: () -> Unit,
    onDiscoverRecipes: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading && uiState.favorites.isNotEmpty(),
        onRefresh = onRefresh
    )
    
    // Show error in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.favorites.isEmpty() -> {
                    // Initial loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.semantics { 
                                contentDescription = "Loading favorites" 
                            }
                        )
                    }
                }
                
                uiState.favorites.isEmpty() -> {
                    // Empty state
                    EmptyFavoritesContent(
                        onDiscoverRecipes = onDiscoverRecipes
                    )
                }
                
                else -> {
                    // Content
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.favorites,
                            key = { it.recipeId?.toString() ?: "" }
                        ) { recipe ->
                            FavoriteRecipeCard(
                                recipe = recipe,
                                onClick = onRecipeClick,
                                onRemove = onRemoveFavorite
                            )
                        }
                    }
                }
            }
            
            // Pull refresh indicator
            PullRefreshIndicator(
                refreshing = uiState.isLoading && uiState.favorites.isNotEmpty(),
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun EmptyFavoritesContent(
    onDiscoverRecipes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_box),
            contentDescription = "Empty favorites illustration",
            modifier = Modifier
                .size(120.dp)
                .semantics { contentDescription = "Empty favorites illustration" }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Recipe Bookmarked Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Don't forget to bookmark your favorite recipe",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onDiscoverRecipes,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Discover Recipes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenEmptyPreview() {
    ChefnutTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                isLoading = false,
                favorites = emptyList()
            ),
            onRecipeClick = {},
            onRemoveFavorite = {},
            onRefresh = {},
            onDiscoverRecipes = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenWithDataPreview() {
    ChefnutTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                isLoading = false,
                favorites = listOf(
                    FavouriteRecipe(
                    id = 1,
                    recipeId = 1,
                        recipeTitle = "Pasta Carbonara",
                        recipeImageUrl = "https://example.com/pasta.jpg",
                        cookingTimeInMinutes = 30,
                        servingCount = 4,
                        source = "Italian Kitchen"
                    ),
                    FavouriteRecipe(
                    id = 2,
                    recipeId = 2,
                        recipeTitle = "Caesar Salad",
                        recipeImageUrl = "https://example.com/salad.jpg",
                        cookingTimeInMinutes = 15,
                        servingCount = 2,
                        source = "Healthy Eats"
                    )
                )
            ),
            onRecipeClick = {},
            onRemoveFavorite = {},
            onRefresh = {}
        )
    }
} 