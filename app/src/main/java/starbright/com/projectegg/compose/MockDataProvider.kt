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
            ),
            // Keep some original recipes for variety
            Recipe(
                id = 6,
                title = "Spaghetti Carbonara",
                image = "https://spoonacular.com/recipeImages/1-556x370.jpg",
                cookingMinutes = 30,
                servingCount = 4,
                sourceStringUrl = "https://example.com/recipe6",
                sourceName = "Chef John's Kitchen",
                cuisines = listOf("Italian"),
                dishTypes = listOf("main course", "dinner")
            ),
            Recipe(
                id = 7,
                title = "Chicken Tikka Masala",
                image = "https://spoonacular.com/recipeImages/2-556x370.jpg",
                cookingMinutes = 45,
                servingCount = 6,
                sourceStringUrl = "https://example.com/recipe7",
                sourceName = "Indian Delights",
                cuisines = listOf("Indian", "Asian"),
                dishTypes = listOf("main course", "dinner")
            ),
            Recipe(
                id = 8,
                title = "Caesar Salad",
                image = "https://spoonacular.com/recipeImages/3-556x370.jpg",
                cookingMinutes = 15,
                servingCount = 2,
                sourceStringUrl = "https://example.com/recipe8",
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
                recipeTitle = "Chicken Pasta Carbonara",
                recipeImageUrl = "https://example.com/chicken-pasta.jpg",
                cookingTimeInMinutes = 30,
                servingCount = 4,
                source = "Italian Kitchen"
            ),
            FavouriteRecipe(
                id = 2,
                recipeId = 2,
                recipeTitle = "Chocolate Lava Cake",
                recipeImageUrl = "https://example.com/chocolate-cake.jpg",
                cookingTimeInMinutes = 25,
                servingCount = 2,
                source = "Dessert Corner"
            )
        )
    }
    
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
            ),
            // Keep some original search terms for variety
            SearchHistory(
                id = 6,
                query = "pasta",
                createdAt = System.currentTimeMillis() - 21600000 // 6 hours ago
            ),
            SearchHistory(
                id = 7,
                query = "chicken",
                createdAt = System.currentTimeMillis() - 25200000 // 7 hours ago
            ),
            SearchHistory(
                id = 8,
                query = "salad",
                createdAt = System.currentTimeMillis() - 28800000 // 8 hours ago
            )
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
            title = "Chicken Pasta Carbonara",
            image = "https://example.com/chicken-pasta.jpg",
            cookingMinutes = 30,
            servingCount = 4,
            sourceStringUrl = "https://example.com/recipe1",
            sourceName = "Italian Kitchen",
            cuisines = listOf("Italian"),
            dishTypes = listOf("Pasta"),
            ingredients = getIngredientsList(),
            instructions = listOf(
                Instruction(
                    name = "Preparation",
                    steps = listOf(
                        "Bring a large pot of salted water to boil",
                        "Mince the garlic",
                        "Grate the parmesan cheese",
                        "Cut chicken into bite-sized pieces"
                    )
                ),
                Instruction(
                    name = "Cooking",
                    steps = listOf(
                        "Cook pasta according to package directions",
                        "While pasta cooks, heat olive oil and cook chicken",
                        "Add garlic and cook until fragrant",
                        "Mix eggs with cheese",
                        "Drain pasta and combine with egg mixture and chicken"
                    )
                )
            )
        )
    }

    fun getRecipeDetailById(recipeId: Int): Recipe? {
        return when (recipeId) {
            1 -> Recipe(
                id = 1,
                title = "Chicken Pasta Carbonara",
                image = "https://example.com/chicken-pasta.jpg",
                cookingMinutes = 30,
                servingCount = 4,
                sourceStringUrl = "https://example.com/recipe1",
                sourceName = "Italian Kitchen",
                cuisines = listOf("Italian"),
                dishTypes = listOf("Pasta"),
                ingredients = getIngredientsList(),
                instructions = listOf(
                    Instruction(
                        name = "Preparation",
                        steps = listOf(
                            "Bring a large pot of salted water to boil",
                            "Mince the garlic",
                            "Grate the parmesan cheese",
                            "Cut chicken into bite-sized pieces"
                        )
                    ),
                    Instruction(
                        name = "Cooking",
                        steps = listOf(
                            "Cook pasta according to package directions",
                            "While pasta cooks, heat olive oil and cook chicken",
                            "Add garlic and cook until fragrant",
                            "Mix eggs with cheese",
                            "Drain pasta and combine with egg mixture and chicken"
                        )
                    )
                )
            )
            2 -> Recipe(
                id = 2,
                title = "Chocolate Lava Cake",
                image = "https://example.com/chocolate-cake.jpg",
                cookingMinutes = 25,
                servingCount = 2,
                sourceStringUrl = "https://example.com/recipe2",
                sourceName = "Dessert Corner",
                cuisines = listOf("French"),
                dishTypes = listOf("Dessert"),
                ingredients = listOf(
                    Ingredient("200g dark chocolate").apply {
                        setId("1")
                        setName("Dark Chocolate")
                        setAmount(200)
                        setUnit("g")
                    },
                    Ingredient("100g butter").apply {
                        setId("2")
                        setName("Butter")
                        setAmount(100)
                        setUnit("g")
                    },
                    Ingredient("3 eggs").apply {
                        setId("3")
                        setName("Eggs")
                        setAmount(3)
                        setUnit("pieces")
                    },
                    Ingredient("100g sugar").apply {
                        setId("4")
                        setName("Sugar")
                        setAmount(100)
                        setUnit("g")
                    },
                    Ingredient("50g flour").apply {
                        setId("5")
                        setName("Flour")
                        setAmount(50)
                        setUnit("g")
                    }
                ),
                instructions = listOf(
                    Instruction(
                        name = "Preparation",
                        steps = listOf(
                            "Preheat oven to 200°C (400°F)",
                            "Butter and flour 4 ramekins",
                            "Melt chocolate and butter together"
                        )
                    ),
                    Instruction(
                        name = "Baking",
                        steps = listOf(
                            "Whisk eggs and sugar until fluffy",
                            "Fold in melted chocolate mixture",
                            "Gently fold in flour",
                            "Pour into ramekins and bake for 12 minutes"
                        )
                    )
                )
            )
            3 -> Recipe(
                id = 3,
                title = "Vegetarian Lasagna",
                image = "https://example.com/vegetarian-lasagna.jpg",
                cookingMinutes = 60,
                servingCount = 6,
                sourceStringUrl = "https://example.com/recipe3",
                sourceName = "Healthy Eats",
                cuisines = listOf("Italian"),
                dishTypes = listOf("Pasta", "Vegetarian"),
                ingredients = listOf(
                    Ingredient("12 lasagna sheets").apply {
                        setId("1")
                        setName("Lasagna Sheets")
                        setAmount(12)
                        setUnit("sheets")
                    },
                    Ingredient("500g ricotta cheese").apply {
                        setId("2")
                        setName("Ricotta Cheese")
                        setAmount(500)
                        setUnit("g")
                    },
                    Ingredient("2 cups spinach").apply {
                        setId("3")
                        setName("Spinach")
                        setAmount(2)
                        setUnit("cups")
                    },
                    Ingredient("1 cup marinara sauce").apply {
                        setId("4")
                        setName("Marinara Sauce")
                        setAmount(1)
                        setUnit("cup")
                    },
                    Ingredient("1 cup mozzarella").apply {
                        setId("5")
                        setName("Mozzarella")
                        setAmount(1)
                        setUnit("cup")
                    }
                ),
                instructions = listOf(
                    Instruction(
                        name = "Preparation",
                        steps = listOf(
                            "Preheat oven to 180°C (350°F)",
                            "Cook lasagna sheets according to package",
                            "Mix ricotta with spinach"
                        )
                    ),
                    Instruction(
                        name = "Assembly",
                        steps = listOf(
                            "Layer lasagna sheets in baking dish",
                            "Spread ricotta mixture",
                            "Add marinara sauce",
                            "Repeat layers and top with mozzarella",
                            "Bake for 45 minutes"
                        )
                    )
                )
            )
            else -> null
        }
    }
}
