/**
 * Created by Andreas on 7/10/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local

import io.reactivex.Observable
import starbright.com.projectegg.data.AppDataStore
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Andreas on 4/8/2018.
 */

@Singleton
class AppLocalDataStore @Inject constructor(private val mDb: RecipeDatabase) : AppDataStore {

    override fun getRecipes(ingredients: String): Observable<List<Recipe>> {
        System.err.print(Constants.OPERATION_NOT_SUPPORTED)
        throw UnsupportedOperationException()
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        System.err.println(Constants.OPERATION_NOT_SUPPORTED)
        throw UnsupportedOperationException()
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return mDb.daoAccess().getRecipe(Integer.parseInt(recipeId)).toObservable()
    }

    override fun saveDetailInformation(recipe: Recipe) {
        mDb.daoAccess().insertRecipe(recipe)
    }
}
