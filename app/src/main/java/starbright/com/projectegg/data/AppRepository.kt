/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
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

    override suspend fun getRecipes(config: RecipeConfig, offset: Int): List<Recipe> {
        return appRemoteDataStore.getRecipes(config, offset)
            .flowOn(Dispatchers.IO)
            .single()
    }

    override suspend fun getRecommendedRecipe(offSet: Int): List<Recipe> {
        return appRemoteDataStore.getRecommendedRecipe(offSet)
            .flowOn(Dispatchers.IO)
            .single()
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        return appRemoteDataStore.searchIngredient(query)
    }

    override suspend fun getRecipeDetailInformation(recipeId: String): Recipe {
        return appRemoteDataStore.getRecipeDetailInformation(recipeId)
            .flowOn(Dispatchers.IO)
            .first()
    }

    override fun saveDetailInformation(recipe: Recipe) {
        appLocalDataStore.saveDetailInformation(recipe)
    }

    override suspend fun removeFavouriteRecipe(recipeId: Int) {
        return appLocalDataStore.removeFavouriteRecipe(recipeId)
    }

    override suspend fun saveFavouriteRecipe(recipe: Recipe) {
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

    override suspend fun getFavouriteRecipe(): Flow<List<FavouriteRecipe>> {
        return appLocalDataStore.getFavouriteRecipeWith()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun isRecipeSavedBefore(recipeId: Int): FavouriteRecipe? {
        return appLocalDataStore.getFavouriteRecipeWith(recipeId)
    }

    override suspend fun getSearchHistory(): Flow<List<SearchHistory>> {
        return appLocalDataStore.getSearchHistory()
            .flowOn(Dispatchers.IO)
    }

    override fun checkQueryExistence(query: String): Flow<List<SearchHistory>> {
        return appLocalDataStore.checkQueryExistence(query)
            .flowOn(Dispatchers.IO)
    }

    override suspend fun updateExistingHistoryTimestamp(query: String, millis: Long) {
        withContext(Dispatchers.IO) {
            appLocalDataStore.updateExistingHistoryTimestamp(query, millis)
        }
    }

    override suspend fun addSearchHistory(history: SearchHistory) {
        withContext(Dispatchers.IO) {
            appLocalDataStore.saveSearchHistory(history)
        }
    }

    override suspend fun removeSearchHistory(query: String) {
        withContext(Dispatchers.IO) {
            appLocalDataStore.removeSearchHistory(query)
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
