/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory

interface RecipeRepository {
    fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>>
    suspend fun getRecommendedRecipe(offSet: Int): List<Recipe>
    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun searchIngredient(query: String): Observable<List<Ingredient>>

    fun saveDetailInformation(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: Int): Completable
    fun saveFavouriteRecipe(recipe: Recipe): Completable
    suspend fun getFavouriteRecipe(): List<FavouriteRecipe>
    fun isRecipeSavedBefore(recipeId: Int): Observable<FavouriteRecipe?>

    fun getSearchHistory(): Maybe<List<SearchHistory>>
    fun checkQueryExistence(query: String): Maybe<List<SearchHistory>>
    fun updateExistingHistoryTimestamp(query: String, millis: Long): Completable
    fun addSearchHistory(history: SearchHistory): Completable
    fun removeSearchHistory(query: String): Completable
}