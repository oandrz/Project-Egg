/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Observable
import starbright.com.projectegg.dagger.qualifier.LocalData
import starbright.com.projectegg.dagger.qualifier.RemoteData
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    @LocalData private val appLocalDataStore: AppDataStore,
    @RemoteData private val appRemoteDataStore: AppDataStore
) : AppDataStore {

    override fun getRecipes(ingredients: String, cuisine: String, offset: Int): Observable<List<Recipe>> {
        return appRemoteDataStore.getRecipes(ingredients, cuisine, offset)
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
}
