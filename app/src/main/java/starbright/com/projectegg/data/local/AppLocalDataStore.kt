/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 21 - 8 - 2020.
 */

package starbright.com.projectegg.data.local

import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.local.database.ApplicationDatabase
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocalDataStore @Inject constructor(
    private val database: ApplicationDatabase
) : AppDataStore {

    override fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>> {
        throw UnsupportedOperationException(Constants.OPERATION_NOT_SUPPORTED)
    }

    override fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>> {
        throw UnsupportedOperationException(Constants.OPERATION_NOT_SUPPORTED)
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        throw UnsupportedOperationException(Constants.OPERATION_NOT_SUPPORTED)
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        throw UnsupportedOperationException(Constants.OPERATION_NOT_SUPPORTED)
    }

    override fun saveDetailInformation(recipe: Recipe) {
        throw UnsupportedOperationException(Constants.OPERATION_NOT_SUPPORTED)
    }

    override fun removeFavouriteRecipe(recipeId: Int): Completable {
        return database.favoriteRecipeDao().removeFavouriteRecipe(recipeId)
    }

    override fun saveFavouriteRecipe(recipe: FavouriteRecipe): Completable {
        return database.favoriteRecipeDao().addFavouriteRecipe(recipe)
    }

    override fun getFavouriteRecipeWith(): Observable<List<FavouriteRecipe>> {
        return database.favoriteRecipeDao().getFavouriteRecipe()
    }

    override fun getFavouriteRecipeWith(recipeId: Int): Observable<FavouriteRecipe?> {
        return database.favoriteRecipeDao().getFavouriteRecipeWith(recipeId)
    }

    override fun getSearchHistory(): Observable<List<SearchHistory>> {
        return database.searchHistoryDao().getSearchHistory()
    }

    override fun saveSearchHistory(history: SearchHistory): Completable {
        return database.searchHistoryDao().addSearchHistory(history)
    }

    override fun removeSearchHistory(query: String): Completable {
        return database.searchHistoryDao().removeSearchHistory(query)
    }
}
