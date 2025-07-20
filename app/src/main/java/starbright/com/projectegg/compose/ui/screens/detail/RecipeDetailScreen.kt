package starbright.com.projectegg.compose.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import starbright.com.projectegg.R
import starbright.com.projectegg.compose.ui.components.IngredientItem
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    uiState: RecipeDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onIngredientChecked: (Int, Boolean) -> Unit,
    onAddToCartClick: () -> Unit,
    onSourceClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Empty title */ },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState.recipe != null) {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (uiState.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Filled.FavoriteBorder
                                },
                                contentDescription = if (uiState.isFavorite) {
                                    "Remove from favorites"
                                } else {
                                    "Add to favorites"
                                },
                                tint = if (uiState.isFavorite) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            if (uiState.recipe != null && uiState.selectedIngredients.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = onAddToCartClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text("Add to Cart (${uiState.selectedIngredients.size})")
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.semantics { 
                                contentDescription = "Loading recipe details" 
                            }
                        )
                    }
                }
                
                uiState.recipe != null -> {
                    // Content
                    RecipeDetailContent(
                        recipe = uiState.recipe,
                        selectedIngredients = uiState.selectedIngredients,
                        onIngredientChecked = onIngredientChecked,
                        onSourceClick = onSourceClick
                    )
                }
                
                uiState.error != null -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = "Error",
                                modifier = Modifier.size(120.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = uiState.error,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeDetailContent(
    recipe: Recipe,
    selectedIngredients: Set<Int>,
    onIngredientChecked: (Int, Boolean) -> Unit,
    onSourceClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Recipe Image
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.empty_view_image),
                error = painterResource(R.drawable.empty_view_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
        
        // Recipe Info
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    InfoChip(
                        label = "${recipe.cookingMinutes ?: 0} Min",
                        icon = R.drawable.ic_time
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoChip(
                        label = "${recipe.servingCount ?: 0} Peoples",
                        icon = R.drawable.ic_serving_small
                    )
                }
                
                if (!recipe.cuisines.isNullOrEmpty() || !recipe.dishTypes.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        recipe.cuisines?.firstOrNull()?.let { cuisine ->
                            Text(
                                text = cuisine,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (!recipe.cuisines.isNullOrEmpty() && !recipe.dishTypes.isNullOrEmpty()) {
                            Text(
                                text = " • ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        recipe.dishTypes?.firstOrNull()?.let { dishType ->
                            Text(
                                text = dishType.replaceFirstChar { if (it.isLowerCase()) it.uppercaseChar() else it },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Ingredients Section
        if (!recipe.ingredients.isNullOrEmpty()) {
            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            recipe.ingredients?.forEach { ingredient ->
                item(key = ingredient.id) {
                    IngredientItem(
                        ingredient = ingredient,
                        isChecked = selectedIngredients.contains(ingredient.id?.toIntOrNull() ?: 0),
                        onCheckedChange = { isChecked ->
                            ingredient.id?.toIntOrNull()?.let { id ->
                                onIngredientChecked(id, isChecked)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
        
        // Instructions Section
        if (!recipe.instructions.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            recipe.instructions?.forEach { instruction ->
                item {
                    InstructionSection(
                        instruction = instruction,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
        
        // Source Section
        if (!recipe.sourceStringUrl.isNullOrBlank()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Recipe Source",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        // Source URL
                        recipe.sourceName?.let { sourceName ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSourceClick() }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_search),
                                    contentDescription = "Source",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sourceName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Bottom spacing for FAB
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    icon: Int
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun InstructionSection(
    instruction: Instruction,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (instruction.name.isNotEmpty()) {
            Text(
                text = instruction.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        instruction.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(24.dp)
                )
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    ChefnutTheme {
        RecipeDetailScreen(
            uiState = RecipeDetailUiState(
                isLoading = false,
                recipe = Recipe(
                    id = 123,
                    title = "Pasta Carbonara",
                    image = "https://example.com/pasta.jpg",
                    cookingMinutes = 30,
                    servingCount = 4,
                    calories = 500,
                    sourceStringUrl = "http://example.com",
                    sourceName = "Italian Kitchen",
                    ingredients = listOf(
                        Ingredient("400g pasta").apply {
                            setId("1")
                            setAmount(400)
                            setUnit("g")
                        },
                        Ingredient("200g bacon").apply {
                            setId("2")
                            setAmount(200)
                            setUnit("g")
                        },
                        Ingredient("3 eggs").apply {
                            setId("3")
                            setAmount(3)
                            setUnit("eggs")
                        }
                    ),
                    instructions = listOf(
                        Instruction(
                            name = "Preparation",
                            steps = listOf(
                                "Boil water for pasta",
                                "Beat eggs in a bowl"
                            )
                        )
                    ),
                    dishTypes = listOf("main course"),
                    cuisines = listOf("Italian")
                ),
                isFavorite = false,
                selectedIngredients = setOf(1)
            ),
            onBackClick = {},
            onFavoriteClick = {},
            onIngredientChecked = { _, _ -> },
            onAddToCartClick = {},
            onSourceClick = {}
        )
    }
} 