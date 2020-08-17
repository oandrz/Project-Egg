/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 17 - 8 - 2020.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Observable
import starbright.com.projectegg.dagger.qualifier.LocalData
import starbright.com.projectegg.dagger.qualifier.RemoteData
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.enum.RecipeSortCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @LocalData private val appLocalDataStore: AppDataStore,
    @RemoteData private val appRemoteDataStore: AppDataStore
) : RecipeRepository {

    override fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>> {
        return appRemoteDataStore.getRecipes(config, offset)
    }

    override fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>> {
        return appRemoteDataStore.getRecommendedRecipe(offSet)
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        return appRemoteDataStore.searchIngredient(query)
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return appRemoteDataStore.getRecipeDetailInformation(recipeId)
    }

    override fun saveDetailInformation(recipe: Recipe) {
        appLocalDataStore.saveDetailInformation(recipe)
    }

    override fun removeFavouriteRecipe(recipeId: Int): Completable {
        return appLocalDataStore.removeFavouriteRecipe(recipeId)
    }

    override fun saveFavouriteRecipe(recipe: Recipe): Completable {
        return recipe.let {
            appLocalDataStore.saveFavouriteRecipe(
                FavouriteRecipe(
                    recipeId = it.id,
                    recipeTitle = it.title,
                    recipeImageUrl = it.image.orEmpty(),
                    cookingTimeInMinutes = it.cookingMinutes ?: 0,
                    servingCount = it.servingCount ?: 0,
                    source = it.sourceName.orEmpty()
                )
            )
        }
    }

    override fun getFavouriteRecipe(): Observable<List<FavouriteRecipe>> {
        return appLocalDataStore.getFavouriteRecipeWith()
    }

    override fun isRecipeSavedBefore(recipeId: Int): Observable<FavouriteRecipe?> {
        return appLocalDataStore.getFavouriteRecipeWith(recipeId)
    }

    override fun getSearchHistory(): Observable<List<SearchHistory>> {
        return appLocalDataStore.getSearchHistory()
    }

    override fun addSearchHistory(query: SearchHistory): Completable {
        return appLocalDataStore.saveSearchHistory(query)
    }
}

data class RecipeConfig(
    var query: String?,
    var cuisine: String?,
    var sortCategory: RecipeSortCategory = RecipeSortCategory.TIME,
    var ingredients: List<Ingredient>?,
    var responseLimit: Int = 10
)
