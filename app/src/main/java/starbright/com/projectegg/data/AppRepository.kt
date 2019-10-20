/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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

    override fun getRecipes(ingredients: String): Observable<List<Recipe>> {
        return appRemoteDataStore.getRecipes(ingredients)
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        return appRemoteDataStore.searchIngredient(query)
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return Observable.mergeDelayError(
                appRemoteDataStore
                        .getRecipeDetailInformation(recipeId)
                        .doOnNext { recipe -> appLocalDataStore.saveDetailInformation(recipe) }
                        .subscribeOn(Schedulers.io()),
                appLocalDataStore.getRecipeDetailInformation(recipeId)
                        .subscribeOn(Schedulers.io())
        )
    }

    override fun saveDetailInformation(recipe: Recipe) {
        appLocalDataStore.saveDetailInformation(recipe)
    }
}
