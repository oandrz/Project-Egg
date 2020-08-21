/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 21 - 8 - 2020.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory

interface RecipeRepository {
    fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>>
    fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>>
    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun searchIngredient(query: String): Observable<List<Ingredient>>

    fun saveDetailInformation(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: Int): Completable
    fun saveFavouriteRecipe(recipe: Recipe): Completable
    fun getFavouriteRecipe(): Observable<List<FavouriteRecipe>>
    fun isRecipeSavedBefore(recipeId: Int): Observable<FavouriteRecipe?>

    fun getSearchHistory(): Observable<List<SearchHistory>>
    fun addSearchHistory(history: SearchHistory): Completable
    fun removeSearchHistory(query: String): Completable
}