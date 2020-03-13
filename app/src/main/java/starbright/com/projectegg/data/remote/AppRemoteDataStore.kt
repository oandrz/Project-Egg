/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.remote

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import starbright.com.projectegg.BuildConfig
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.response.IngredientResponse
import starbright.com.projectegg.data.model.response.RecipeDetailResponse
import starbright.com.projectegg.data.model.response.RecipeListResponse
import starbright.com.projectegg.data.model.response.RecipeResponse
import starbright.com.projectegg.util.Constants
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRemoteDataStore @Inject constructor(private val mRetrofit: Retrofit) : AppDataStore {

    override fun getRecipes(ingredients: String, offset: Int): Observable<List<Recipe>> {
        val queryMap = HashMap<String, String>()
        queryMap[Constants.QUERY_PARAM_LIMIT_LICENSE_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_INSTRUCTION_REQUIRED_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_ADD_INFORMATION] = true.toString()
        queryMap[Constants.QUERY_PARAM_SORT_KEY] = "time"
        return mRetrofit.create(Service::class.java)
            .getRecipes(ingredients = ingredients, offset = offset, options =queryMap)
            .map { responses ->
                val recipes = ArrayList<Recipe>(responses.results.size)
                for (response in responses.results) {
                    recipes.add(Recipe(response))
                }
                recipes
            }
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        val numberOfIngredient = 5
        val queryMap = HashMap<String, String>()
        queryMap[Constants.QUERY_PARAM_META_INFORMATION_KEY] = true.toString()
        queryMap[Constants.QUERY_PARAM_NUMBER_KEY] = numberOfIngredient.toString()
        return mRetrofit.create(Service::class.java).searchAutocompleteIngredients(query = query, options = queryMap)
            .map { responses ->
                val ingredients = ArrayList<Ingredient>(responses.size)
                for (response in responses) {
                    ingredients.add(Ingredient(response))
                }
                ingredients
            }
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return mRetrofit.create(Service::class.java).getRecipeDetailInformation(recipeId = recipeId)
            .map { recipeDetailResponse -> Recipe(recipeDetailResponse) }
    }

    override fun saveDetailInformation(recipe: Recipe) {
        throw UnsupportedOperationException()
    }

    private interface Service {
        @GET("recipes/complexSearch")
        fun getRecipes(
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
            @Query("includeIngredients") ingredients: String,
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
            @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY
        ): Observable<RecipeDetailResponse>
    }
}