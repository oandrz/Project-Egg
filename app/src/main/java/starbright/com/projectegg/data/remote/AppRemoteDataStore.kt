/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 22 - 8 - 2020.
 */

package starbright.com.projectegg.data.remote

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
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

    override fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>> {
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

        return retrofit.create(Service::class.java)
            .getRecipes(
                query = config.query,
                ingredients = ingredientsAsParam.toString(),
                cuisine = config.cuisine.orEmpty(),
                offset = offset,
                options = queryMap
            )
            .map { responses ->
                val recipes = ArrayList<Recipe>(responses.results.size)
                for (response in responses.results) {
                    recipes.add(
                        Recipe(
                            id = response.id,
                            title = response.title,
                            image = response.image,
                            cuisines = response.cuisines,
                            cookingMinutes = response.cookingTime,
                            servingCount = response.servings,
                            totalRecipe = responses.totalResults
                        )
                    )
                }
                recipes
            }
    }

    override fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>> {
        val queryMap = HashMap<String, String>()
        queryMap[Constants.QUERY_PARAM_LIMIT_LICENSE_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_INSTRUCTION_REQUIRED_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_ADD_INFORMATION] = true.toString()
        queryMap[Constants.QUERY_PARAM_SORT_KEY] = RecipeSortCategory.RANDOM.type.toLowerCase()
        return retrofit.create(Service::class.java)
            .getRecommendedRecipes(offset = offSet, options = queryMap)
            .map { responses ->
                val recipes = ArrayList<Recipe>(responses.results.size)
                for (response in responses.results) {
                    response.apply {
                        recipes.add(
                            Recipe(
                                id = id,
                                cookingMinutes = cookingTime,
                                servingCount = servings,
                                title = title,
                                image = image,
                                sourceStringUrl = sourceStringUrl,
                                sourceName = sourceName,
                                dishTypes = dishTypes,
                                cuisines = cuisines
                            )
                        )
                    }
                }
                recipes
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

    override fun getFavouriteRecipeWith(): Observable<List<FavouriteRecipe>> {
        throw UnsupportedOperationException()
    }

    override fun getFavouriteRecipeWith(recipeId: Int): Observable<FavouriteRecipe?> {
        throw UnsupportedOperationException()
    }

    override fun getSearchHistory(): Maybe<List<SearchHistory>> {
        throw UnsupportedOperationException()
    }

    override fun checkQueryExistence(query: String): Maybe<List<SearchHistory>> {
        throw UnsupportedOperationException()
    }

    override fun updateExistingHistoryTimestamp(query: String, millis: Long): Completable {
        throw UnsupportedOperationException()
    }

    override fun saveSearchHistory(history: SearchHistory): Completable {
        throw UnsupportedOperationException()
    }

    override fun removeSearchHistory(query: String): Completable {
        throw UnsupportedOperationException()
    }

    private interface Service {
        @GET("recipes/complexSearch")
        fun getRecipes(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("query") query: String?,
            @Query("includeIngredients") ingredients: String,
            @Query("cuisine") cuisine: String,
            @Query("offset") offset: Int,
            @QueryMap options: Map<String, String>
        ): Observable<RecipeListResponse>

        @GET("recipes/complexSearch")
        fun getRecommendedRecipes(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("offset") offset: Int,
            @QueryMap options: Map<String, String>
        ): Observable<RecipeListResponse>

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