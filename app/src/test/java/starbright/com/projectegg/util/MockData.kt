/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 17/11/2019.
 */

package starbright.com.projectegg.util

import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe

val mockIngredient: Ingredient = Ingredient("apple")

val mockIngredients: List<Ingredient> = listOf(mockIngredient)

val mockRecipe: Recipe = Recipe(0, "testing", "testing", "testing", 0, 0, 10)

val mockRecipeWithoutUrl: Recipe = Recipe(0, "testing", "testing", "testing", 0, 0, 10)

val mockRecipes: List<Recipe> = listOf(mockRecipe)