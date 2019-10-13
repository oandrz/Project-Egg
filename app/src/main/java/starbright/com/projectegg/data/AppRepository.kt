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
        @LocalData private val mAppLocalDatastore: AppDataStore,
        @RemoteData private val mAppRemoteDatastore: AppDataStore
) : AppDataStore {

    override fun getRecipes(ingredients: String): Observable<List<Recipe>> {
        return mAppRemoteDatastore.getRecipes(ingredients)
    }

    override fun searchIngredient(query: String): Observable<List<Ingredient>> {
        return mAppRemoteDatastore.searchIngredient(query)
    }

    override fun getRecipeDetailInformation(recipeId: String): Observable<Recipe> {
        return Observable.mergeDelayError(
                mAppRemoteDatastore
                        .getRecipeDetailInformation(recipeId)
                        .doOnNext { recipe -> mAppLocalDatastore.saveDetailInformation(recipe) }
                        .subscribeOn(Schedulers.io()),
                mAppLocalDatastore.getRecipeDetailInformation(recipeId)
                        .subscribeOn(Schedulers.io())
        )
    }

    override fun saveDetailInformation(recipe: Recipe) {
        mAppLocalDatastore.saveDetailInformation(recipe)
    }
}
