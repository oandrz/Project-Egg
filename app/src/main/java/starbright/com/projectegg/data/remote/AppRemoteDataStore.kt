/**
 * Created by Andreas on 7/10/2018.
 */

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
import starbright.com.projectegg.data.model.response.RecipeResponse
import starbright.com.projectegg.util.Constants
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRemoteDataStore @Inject constructor(private val mRetrofit: Retrofit) : AppDataStore {

    override fun getRecipes(ingredients: String): Observable<List<Recipe>> {
        return mRetrofit.create(Service::class.java).getRecipes(ingredients = ingredients, options = HashMap())
                .map { responses ->
                    val recipes = ArrayList<Recipe>(responses.size)
                    for (response in responses) {
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
        @GET("recipes/findByIngredients")
        fun getRecipes(
                @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
                @Query("ingredients") ingredients: String,
                @QueryMap options: Map<String, String>
        ): Observable<List<RecipeResponse>>

        @GET("food/ingredients/autocomplete")
        fun searchAutocompleteIngredients(
                @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
                @Query("query") query: String,
                @QueryMap options: Map<String, String>
        ): Observable<List<IngredientResponse>>

        @GET("recipes/{recipeId}/information")
        fun getRecipeDetailInformation(
                @Query(Constants.QUERY_API_KEY) apiKey: String? = BuildConfig.SPOON_KEY,
                @Path("recipeId") recipeId: String
        ): Observable<RecipeDetailResponse>
    }
}