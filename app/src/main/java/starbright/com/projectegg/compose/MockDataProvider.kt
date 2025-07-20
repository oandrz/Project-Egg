package starbright.com.projectegg.compose

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory

/**
 * Provides mock data for testing Compose screens without backend/DI
 */
object MockDataProvider {
    
    fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = 1,
                title = "Spaghetti Carbonara",
                image = "https://spoonacular.com/recipeImages/1-556x370.jpg",
                cookingMinutes = 30,
                servingCount = 4,
                sourceStringUrl = "https://example.com/recipe1",
                sourceName = "Chef John's Kitchen",
                cuisines = listOf("Italian"),
                dishTypes = listOf("main course", "dinner")
            ),
            Recipe(
                id = 2,
                title = "Chicken Tikka Masala",
                image = "https://spoonacular.com/recipeImages/2-556x370.jpg",
                cookingMinutes = 45,
                servingCount = 6,
                sourceStringUrl = "https://example.com/recipe2",
                sourceName = "Indian Delights",
                cuisines = listOf("Indian", "Asian"),
                dishTypes = listOf("main course", "dinner")
            ),
            Recipe(
                id = 3,
                title = "Caesar Salad",
                image = "https://spoonacular.com/recipeImages/3-556x370.jpg",
                cookingMinutes = 15,
                servingCount = 2,
                sourceStringUrl = "https://example.com/recipe3",
                sourceName = "Healthy Eats",
                cuisines = listOf("American"),
                dishTypes = listOf("salad", "side dish")
            )
        )
    }
    
    fun getFavorites(): List<FavouriteRecipe> {
        return listOf(
            FavouriteRecipe(
                id = 1,
                recipeId = 1,
                recipeTitle = "Spaghetti Carbonara",
                recipeImageUrl = "https://spoonacular.com/recipeImages/1-556x370.jpg",
                cookingTimeInMinutes = 30,
                servingCount = 4,
                source = "Chef John's Kitchen"
            ),
            FavouriteRecipe(
                id = 2,
                recipeId = 2,
                recipeTitle = "Chicken Tikka Masala",
                recipeImageUrl = "https://spoonacular.com/recipeImages/2-556x370.jpg",
                cookingTimeInMinutes = 45,
                servingCount = 6,
                source = "Indian Delights"
            )
        )
    }
    
    fun getSearchHistory(): List<SearchHistory> {
        return listOf(
            SearchHistory(id = 1, query = "pasta", createdAt = System.currentTimeMillis()),
            SearchHistory(id = 2, query = "chicken", createdAt = System.currentTimeMillis() - 3600000),
            SearchHistory(id = 3, query = "salad", createdAt = System.currentTimeMillis() - 7200000),
            SearchHistory(id = 4, query = "soup", createdAt = System.currentTimeMillis() - 10800000)
        )
    }
    
    fun getIngredients(): List<Pair<Recipe, Boolean>> {
        return listOf(
            Recipe(1, title = "Pasta", image = null) to false,
            Recipe(2, title = "Olive Oil", image = null) to true,
            Recipe(3, title = "Garlic", image = null) to false,
            Recipe(4, title = "Parmesan", image = null) to true,
            Recipe(5, title = "Black Pepper", image = null) to false
        )
    }
    
    fun getIngredientsList(): List<Ingredient> {
        return listOf(
            Ingredient("200g pasta").apply {
                setId("1")
                setAmount(200)
                setUnit("g")
            },
            Ingredient("2 tbsp olive oil").apply {
                setId("2")
                setName("Olive Oil")
                setAmount(2)
                setUnit("tbsp")
            },
            Ingredient("3 garlic cloves").apply {
                setId("3")
                setName("Garlic")
                setAmount(3)
                setUnit("cloves")
            },
            Ingredient("1/4 cup white wine").apply {
                setId("4")
                setName("White Wine")
                setAmount(0.25f.toInt())
                setUnit("cup")
            },
            Ingredient("1 cup cherry tomatoes").apply {
                setId("5")
                setName("Cherry Tomatoes")
                setAmount(1)
                setUnit("cup")
            },
            Ingredient("Fresh basil").apply {
                setId("6")
                setName("Fresh Basil")
                setUnit("to taste")
            },
            Ingredient("Salt and pepper").apply {
                setId("7")
                setName("Salt and Pepper")
                setUnit("to taste")
            },
            Ingredient("Parmesan cheese").apply {
                setId("8")
                setName("Parmesan Cheese")
                setUnit("for serving")
            }
        )
    }
    
    fun getRecipeDetail(): Recipe {
        return Recipe(
            id = 1,
            title = "Spaghetti Carbonara",
            image = "https://spoonacular.com/recipeImages/1-556x370.jpg",
            cookingMinutes = 30,
            servingCount = 4,
            sourceStringUrl = "https://example.com/recipe1",
            sourceName = "Chef John's Kitchen",
            cuisines = listOf("Italian"),
            dishTypes = listOf("main course", "dinner"),
            ingredients = getIngredientsList(),
            instructions = listOf(
                Instruction(
                    name = "Preparation",
                    steps = listOf(
                        "Bring a large pot of salted water to boil",
                        "Mince the garlic",
                        "Grate the parmesan cheese"
                    )
                ),
                Instruction(
                    name = "Cooking",
                    steps = listOf(
                        "Cook pasta according to package directions",
                        "While pasta cooks, heat olive oil and cook garlic",
                        "Mix eggs with cheese",
                        "Drain pasta and combine with egg mixture"
                    )
                )
            )
        )
    }
}
