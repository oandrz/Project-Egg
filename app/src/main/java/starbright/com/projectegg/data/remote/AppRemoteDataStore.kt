package starbright.com.projectegg.data.remote

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import starbright.com.projectegg.MyApp
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.data.local.model.Recipe
import starbright.com.projectegg.data.local.model.response.IngredientResponse
import starbright.com.projectegg.data.local.model.response.RecipeDetailResponse
import starbright.com.projectegg.data.local.model.response.RecipeResponse
import starbright.com.projectegg.util.Constants
import java.util.*
import javax.inject.Inject

/**
 * Created by Andreas on 4/8/2018.
 */

class AppRemoteDataStore : AppDataStore {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        MyApp.getAppComponent().inject(this)
    }

    override fun getRecipes(ingredients: String): Observable<List<Recipe>> {
        return retrofit
                .create(Service::class.java)
                .getRecipes(ingredients, HashMap())
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
        return retrofit
                .create(Service::class.java)
                .searchAutocompleteIngredients(query, queryMap)
                .map { responses ->
                    val ingredients = ArrayList<Ingredient>(responses.size)
                    for (response in responses) {
                        ingredients.add(Ingredient(response))
                    }
                    ingredients
                }
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return retrofit
                .create(Service::class.java)
                .getRecipeDetailInformation(recipeId)
                .map { recipeDetailResponse -> Recipe(recipeDetailResponse) }
    }

    override fun saveDetailInformation(recipe: Recipe) {
        throw UnsupportedOperationException()
    }

    private interface Service {
        @GET("recipes/findByIngredients")
        fun getRecipes(
                @Query("ingredients") ingredients: String,
                @QueryMap options: Map<String, String>
        ): Observable<List<RecipeResponse>>

        @GET("food/ingredients/autocomplete")
        fun searchAutocompleteIngredients(
                @Query("query") query: String,
                @QueryMap options: Map<String, String>
        ): Observable<List<IngredientResponse>>

        @GET("recipes/{recipeId}/information")
        fun getRecipeDetailInformation(
                @Path("recipeId") recipeId: String
        ): Observable<RecipeDetailResponse>
    }
}