/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data.remote

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import starbright.com.projectegg.BuildConfig
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Instruction
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.local.FavouriteRecipe
import starbright.com.projectegg.data.model.local.SearchHistory
import starbright.com.projectegg.data.model.response.IngredientResponse
import starbright.com.projectegg.data.model.response.RecipeDetailResponse
import starbright.com.projectegg.data.model.response.RecipeListResponse
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.util.Constants
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRemoteDataStore @Inject constructor(
    private val retrofit: Retrofit
) : AppDataStore {

    override suspend fun getRecipes(config: RecipeConfig, offset: Int): Flow<List<Recipe>> {
        val queryMap = HashMap<String, String>().apply {
            this[Constants.QUERY_PARAM_LIMIT_LICENSE_KEY] = true.toString()
            this[Constants.QUERY_PARAM_INSTRUCTION_REQUIRED_KEY] = true.toString()
            this[Constants.QUERY_PARAM_ADD_INFORMATION] = true.toString()
            this[Constants.QUERY_PARAM_SORT_KEY] =
                config.sortCategory.type.toLowerCase(Locale.getDefault())
            this[Constants.QUERY_PARAM_NUMBER_KEY] = config.responseLimit.toString()
        }

        val ingredientsAsParam = StringBuilder()
        config.ingredients?.let {
            it.mapIndexed { index, ingredient ->
                ingredientsAsParam.append(ingredient.name)
                if (index < it.size - 1) {
                    ingredientsAsParam.append(", ")
                }
            }
        }

        return flow {
            val response = retrofit.create(Service::class.java)
                .getRecipes(
                    query = config.query,
                    ingredients = ingredientsAsParam.toString(),
                    cuisine = config.cuisine.orEmpty(),
                    offset = offset,
                    options = queryMap
                )

            emit(
                response.results
                    .map {
                        Recipe(
                            id = it.id,
                            title = it.title,
                            image = it.image,
                            cuisines = it.cuisines,
                            cookingMinutes = it.cookingTime,
                            servingCount = it.servings,
                            totalRecipe = response.totalResults
                        )
                    }
            )
        }
    }

    override suspend fun getRecommendedRecipe(offSet: Int): Flow<List<Recipe>> {
        val queryMap = HashMap<String, String>()
        queryMap[Constants.QUERY_PARAM_LIMIT_LICENSE_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_INSTRUCTION_REQUIRED_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_ADD_INFORMATION] = true.toString()
        queryMap[Constants.QUERY_PARAM_SORT_KEY] = RecipeSortCategory.RANDOM.type.toLowerCase()
        return flow {
            emit(
                retrofit.create(Service::class.java)
                    .getRecommendedRecipes(offset = offSet, options = queryMap).results
                    .map {
                        Recipe(
                            id = it.id,
                            cookingMinutes = it.cookingTime,
                            servingCount = it.servings,
                            title = it.title,
                            image = it.image,
                            sourceStringUrl = it.sourceStringUrl,
                            sourceName = it.sourceName,
                            dishTypes = it.dishTypes,
                            cuisines = it.cuisines
                        )
                    }
            )
        }
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        val numberOfIngredient = 5
        val queryMap = HashMap<String, String>()
        queryMap[Constants.QUERY_PARAM_META_INFORMATION_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_NUMBER_KEY] = numberOfIngredient.toString()
        return retrofit.create(Service::class.java)
            .searchAutocompleteIngredients(query = query, options = queryMap)
            .map { responses ->
                val ingredients = ArrayList<Ingredient>(responses.size)
                for (response in responses) {
                    ingredients.add(Ingredient(response))
                }
                ingredients
            }
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        val queryMap = HashMap<String, String>().also {
            it[Constants.QUERY_PARAM_INCL_NUTRITION] = true.toString()
        }
        return retrofit.create(Service::class.java)
            .getRecipeDetailInformation(recipeId = recipeId, options = queryMap)
            .map { recipeDetailResponse ->
                Recipe(
                    id = recipeDetailResponse.id,
                    cookingMinutes = recipeDetailResponse.cookingTime,
                    servingCount = recipeDetailResponse.servings,
                    title = recipeDetailResponse.title,
                    image = recipeDetailResponse.imageStringUrl,
                    sourceStringUrl = recipeDetailResponse.sourceStringUrl,
                    sourceName = recipeDetailResponse.sourceName,
                    ingredients = recipeDetailResponse.extendedIngredientResponse.map {
                        Ingredient(it)
                    },
                    instructions = recipeDetailResponse.analyzedInstructions.first().stepResponse.map {
                        Instruction(it.number, it.step)
                    },
                    calories = recipeDetailResponse.nutrients.nutrients.first().amount.toInt(),
                    dishTypes = recipeDetailResponse.dishTypes,
                    cuisines = recipeDetailResponse.cuisines
                )
            }
    }

    override fun saveDetailInformation(recipe: Recipe) {
        throw UnsupportedOperationException()
    }

    override fun removeFavouriteRecipe(recipeId: Int): Completable {
        throw UnsupportedOperationException()
    }

    override fun saveFavouriteRecipe(recipe: FavouriteRecipe): Completable {
        throw UnsupportedOperationException()
    }

    override suspend fun getFavouriteRecipeWith(): Flow<List<FavouriteRecipe>> {
        throw UnsupportedOperationException()
    }

    override fun getFavouriteRecipeWith(recipeId: Int): Observable<FavouriteRecipe?> {
        throw UnsupportedOperationException()
    }

    override suspend fun getSearchHistory(): Flow<List<SearchHistory>> {
        throw UnsupportedOperationException()
    }

    override fun checkQueryExistence(query: String): Flow<List<SearchHistory>> {
        throw UnsupportedOperationException()
    }

    override suspend fun updateExistingHistoryTimestamp(query: String, millis: Long) {
        throw UnsupportedOperationException()
    }

    override suspend fun saveSearchHistory(history: SearchHistory) {
        throw UnsupportedOperationException()
    }

    override suspend fun removeSearchHistory(query: String) {
        throw UnsupportedOperationException()
    }

    private interface Service {
        @GET("recipes/complexSearch")
        suspend fun getRecipes(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("query") query: String?,
            @Query("includeIngredients") ingredients: String,
            @Query("cuisine") cuisine: String,
            @Query("offset") offset: Int,
            @QueryMap options: Map<String, String>
        ): RecipeListResponse

        @GET("recipes/complexSearch")
        suspend fun getRecommendedRecipes(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("offset") offset: Int,
            @QueryMap options: Map<String, String>
        ): RecipeListResponse

        @GET("food/ingredients/autocomplete")
        fun searchAutocompleteIngredients(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("query") query: String,
            @QueryMap options: Map<String, String>
        ): Observable<List<IngredientResponse>>

        @GET("recipes/{recipeId}/information")
        fun getRecipeDetailInformation(
            @Path("recipeId") recipeId: String,
            @QueryMap options: Map<String, String>,
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY
        ): Observable<RecipeDetailResponse>
    }
}