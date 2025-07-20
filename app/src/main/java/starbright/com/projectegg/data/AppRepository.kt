/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import starbright.com.projectegg.dagger.qualifier.LocalData
import starbright.com.projectegg.dagger.qualifier.RemoteData
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.response.RecipeListResponse
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

    override fun getSearchHistory(): Maybe<List<SearchHistory>> {
        return appLocalDataStore.getSearchHistory()
    }

    override fun checkQueryExistence(query: String): Maybe<List<SearchHistory>> {
        return appLocalDataStore.checkQueryExistence(query)
    }

    override fun updateExistingHistoryTimestamp(query: String, millis: Long): Completable {
        return appLocalDataStore.updateExistingHistoryTimestamp(query, millis)
    }

    override fun addSearchHistory(history: SearchHistory): Completable {
        return appLocalDataStore.saveSearchHistory(history)
    }

    override fun removeSearchHistory(query: String): Completable {
        return appLocalDataStore.removeSearchHistory(query)
    }

    override fun checkIfRecipeIsFavourite(recipeId: Int): Single<Boolean> {
        return appLocalDataStore.checkIfRecipeIsFavourite(recipeId)
    }
    
    override fun deleteFavouriteRecipeById(recipeId: Int): Completable {
        return appLocalDataStore.deleteFavouriteRecipeById(recipeId)
    }
    
    override fun insertFavouriteRecipe(recipe: FavouriteRecipe): Completable {
        return appLocalDataStore.insertFavouriteRecipe(recipe)
    }
    
    override fun loadFavouriteRecipe(): Observable<List<FavouriteRecipe>> {
        return appLocalDataStore.loadFavouriteRecipe()
    }
    
    override fun loadSearchHistory(): Observable<List<SearchHistory>> {
        return appLocalDataStore.loadSearchHistory()
    }
    
    override fun insertSearchHistory(searchHistory: SearchHistory): Completable {
        return appLocalDataStore.insertSearchHistory(searchHistory)
    }
    
    override fun deleteSearchHistoryById(id: Int): Completable {
        return appLocalDataStore.deleteSearchHistoryById(id)
    }

    override fun getRandomRecipe(number: Int): Observable<RecipeListResponse> {
        // Use getRecommendedRecipe and map to RecipeListResponse
        return getRecommendedRecipe(0).map { recipes ->
            RecipeListResponse(
                results = recipes.map { recipe ->
                    starbright.com.projectegg.data.model.response.RecipeResponse(
                        id = recipe.id,
                        title = recipe.title,
                        image = recipe.image,
                        cuisines = recipe.cuisines ?: emptyList(),
                        sourceStringUrl = recipe.sourceStringUrl,
                        sourceName = recipe.sourceName,
                        cookingTime = recipe.cookingMinutes ?: 0,
                        servings = recipe.servingCount ?: 0,
                        dishTypes = recipe.dishTypes ?: emptyList()
                    )
                },
                totalResults = recipes.firstOrNull()?.totalRecipe ?: recipes.size
            )
        }
    }

    override fun getSearchRecipes(query: String, number: Int, offset: Int): Observable<RecipeListResponse> {
        val config = RecipeConfig(
            query = query,
            cuisine = null,
            sortCategory = RecipeSortCategory.TIME,
            ingredients = null,
            responseLimit = number
        )
        return getRecipes(config, offset).map { recipes ->
            RecipeListResponse(
                results = recipes.map { recipe ->
                    starbright.com.projectegg.data.model.response.RecipeResponse(
                        id = recipe.id,
                        title = recipe.title,
                        image = recipe.image,
                        cuisines = recipe.cuisines ?: emptyList(),
                        sourceStringUrl = recipe.sourceStringUrl,
                        sourceName = recipe.sourceName,
                        cookingTime = recipe.cookingMinutes ?: 0,
                        servings = recipe.servingCount ?: 0,
                        dishTypes = recipe.dishTypes ?: emptyList()
                    )
                },
                totalResults = recipes.firstOrNull()?.totalRecipe ?: recipes.size
            )
        }
    }
}

data class RecipeConfig(
    var query: String?,
    var cuisine: String?,
    var sortCategory: RecipeSortCategory = RecipeSortCategory.TIME,
    var ingredients: List<Ingredient>?,
    var responseLimit: Int = 10
)