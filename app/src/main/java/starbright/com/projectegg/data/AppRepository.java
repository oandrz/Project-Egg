/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import starbright.com.projectegg.data.local.AppLocalDataStore;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.data.remote.AppRemoteDataStore;

/**
 * Created by Andreas on 4/8/2018.
 */

public class AppRepository implements AppDataStore {

    private AppLocalDataStore mAppLocalDatastore;
    private AppRemoteDataStore mAppRemoteDatastore;

    @Inject
    public AppRepository(AppLocalDataStore appLocalDatastore, AppRemoteDataStore appRemoteDatastore) {
        mAppLocalDatastore = appLocalDatastore;
        mAppRemoteDatastore = appRemoteDatastore;
    }

    @Override
    public Observable<List<Recipe>> getRecipes(String ingredients) {
        return mAppRemoteDatastore.getRecipes(ingredients);
    }

    @Override
    public Observable<List<Ingredient>> searchIngredient(String query) {
        return mAppRemoteDatastore.searchIngredient(query);
    }

    @Override
    public Observable<Recipe> getRecipeDetailInformation(String recipeId) {
        return Observable.mergeDelayError(
                mAppRemoteDatastore
                        .getRecipeDetailInformation(recipeId)
                        .doOnNext(new Consumer<Recipe>() {
                            @Override
                            public void accept(Recipe recipe) {
                                mAppLocalDatastore.saveDetailInformation(recipe);
                            }
                        })
                        .subscribeOn(Schedulers.io()),
                mAppLocalDatastore.getRecipeDetailInformation(recipeId)
                        .subscribeOn(Schedulers.io())
        );
    }

    @Override
    public void saveDetailInformation(Recipe recipe) {
        mAppLocalDatastore.saveDetailInformation(recipe);
    }
}
