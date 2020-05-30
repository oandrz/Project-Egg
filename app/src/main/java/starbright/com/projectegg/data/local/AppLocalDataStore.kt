/**
 * Created by Andreas on 7/10/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local

import io.reactivex.Observable
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocalDataStore @Inject constructor() : AppDataStore {

    override fun getRecipes(config: RecipeConfig, offset: Int): Observable<List<Recipe>> {
        System.err.print(Constants.OPERATION_NOT_SUPPORTED)
        throw UnsupportedOperationException()
    }

    override fun getRecommendedRecipe(offSet: Int): Observable<List<Recipe>> {
        throw UnsupportedOperationException()
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        System.err.println(Constants.OPERATION_NOT_SUPPORTED)
        throw UnsupportedOperationException()
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        throw UnsupportedOperationException()
    }

    override fun saveDetailInformation(recipe: Recipe) {
        throw UnsupportedOperationException()
    }
}
