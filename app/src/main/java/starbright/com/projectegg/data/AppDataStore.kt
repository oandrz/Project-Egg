/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
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

    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun saveDetailInformation(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: Int): Completable
    fun saveFavouriteRecipe(recipe: FavouriteRecipe): Completable
    suspend fun getFavouriteRecipeWith(): Flow<List<FavouriteRecipe>>
    fun getFavouriteRecipeWith(recipeId: Int): Observable<FavouriteRecipe?>

    suspend fun getSearchHistory(): Flow<List<SearchHistory>>
    fun checkQueryExistence(query: String): Flow<List<SearchHistory>>
    suspend fun updateExistingHistoryTimestamp(query: String, millis: Long)
    suspend fun saveSearchHistory(history: SearchHistory)
    suspend fun removeSearchHistory(query: String)
}
