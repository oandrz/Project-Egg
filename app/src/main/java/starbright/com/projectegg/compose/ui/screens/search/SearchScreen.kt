package starbright.com.projectegg.compose.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import starbright.com.projectegg.compose.ui.components.RecipeCard
import starbright.com.projectegg.compose.ui.components.SearchHistoryItem
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.SearchHistory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onSearchHistoryClick: (String) -> Unit,
    onDeleteSearchHistory: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        TopAppBar(
            title = {
                TextField(
                    value = uiState.currentQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = { Text("Search recipes...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        if (uiState.currentQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                uiState.isSearching -> {
                    // Loading state
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.searchResults.isNotEmpty() -> {
                    // Search results
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.searchResults) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = onRecipeClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                uiState.searchHistory.isNotEmpty() && uiState.currentQuery.isEmpty() -> {
                    // Search history
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Recent Searches",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(uiState.searchHistory) { history ->
                            SearchHistoryItem(
                                searchHistory = history,
                                onItemClick = { onSearchHistoryClick(history.query) },
                                onDeleteClick = { onDeleteSearchHistory(history.id ?: 0) }
                            )
                        }
                    }
                }
                uiState.error != null -> {
                    // Error state
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
                else -> {
                    // Empty state
                    Text(
                        text = if (uiState.currentQuery.isNotEmpty()) 
                            "No recipes found" 
                        else 
                            "Start searching for recipes",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    ChefnutTheme {
        SearchScreen(
            uiState = SearchUiState(
                isSearching = false,
                currentQuery = "chicken",
                searchResults = listOf(
                    Recipe(
                        id = 1,
                        title = "Chicken Parmesan",
                        image = "https://example.com/chicken.jpg",
                        cookingMinutes = 45,
                        servingCount = 4,
                        sourceStringUrl = "http://example.com",
                        sourceName = "Test Kitchen",
                        cuisines = emptyList(),
                        dishTypes = emptyList()
                    )
                )
            ),
            onSearchQueryChanged = {},
            onBackClick = {},
            onRecipeClick = {},
            onSearchHistoryClick = {},
            onDeleteSearchHistory = {}
        )
    }
}

