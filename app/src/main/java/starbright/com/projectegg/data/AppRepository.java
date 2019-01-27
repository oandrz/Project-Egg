package starbright.com.projectegg.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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
}
