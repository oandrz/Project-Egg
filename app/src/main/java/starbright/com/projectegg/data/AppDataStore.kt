/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 31 - 7 - 2020.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe

interface AppDataStore {
    fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>>
    fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>>
    fun searchIngredient(query: String): Observable<List<Ingredient>>

    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun saveDetailInformation(recipe: Recipe)

    fun removeFavouriteRecipe(recipeId: Int): Completable
    fun saveFavouriteRecipe(recipe: FavouriteRecipe): Completable
    fun getFavouriteRecipeWith(): Observable<List<FavouriteRecipe>>
    fun getFavouriteRecipeWith(recipeId: Int): Observable<FavouriteRecipe?>
}
