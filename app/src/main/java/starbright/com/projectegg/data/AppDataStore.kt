/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory

interface AppDataStore {
    suspend fun getRecipes(config: RecipeConfig, offset: Int): Flow<List<Recipe>>
    suspend fun getRecommendedRecipe(offSet: Int): Flow<List<Recipe>>
    fun searchIngredient(query: String): Observable<List<Ingredient>>

    suspend fun getRecipeDetailInformation(recipeId: String): Flow<Recipe>

    fun saveDetailInformation(recipe: Recipe)

    suspend fun removeFavouriteRecipe(recipeId: Int)
    suspend fun saveFavouriteRecipe(recipe: FavouriteRecipe)
    suspend fun getFavouriteRecipeWith(): Flow<List<FavouriteRecipe>>
    suspend fun getFavouriteRecipeWith(recipeId: Int): FavouriteRecipe?

    suspend fun getSearchHistory(): Flow<List<SearchHistory>>
    fun checkQueryExistence(query: String): Flow<List<SearchHistory>>
    suspend fun updateExistingHistoryTimestamp(query: String, millis: Long)
    suspend fun saveSearchHistory(history: SearchHistory)
    suspend fun removeSearchHistory(query: String)
}
