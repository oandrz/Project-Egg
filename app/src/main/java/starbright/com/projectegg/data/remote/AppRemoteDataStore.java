package starbright.com.projectegg.data.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.AppLocalDataStore;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.data.local.model.response.BaseResponse;
import starbright.com.projectegg.data.local.model.response.RecipeResponse;

/**
 * Created by Andreas on 4/8/2018.
 */

public class AppRemoteDataStore implements AppDataStore {
    @Inject
    Retrofit retrofit;

    @Inject
    AppLocalDataStore appLocalDataStore;

    public AppRemoteDataStore() {
        MyApp.getAppComponent().inject(this);
    }

    @Override
    public Observable<List<Recipe>> getRecipes(String ingredients) {
        return retrofit.create(Service.class).getRecipes(ingredients, new HashMap<String, String>())
                .map(new Function<List<RecipeResponse>, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(List<RecipeResponse> responses) throws Exception {
                        List<Recipe> recipes = new ArrayList<>(responses.size());
                        for (RecipeResponse response : responses) {
                            recipes.add(new Recipe(response));
                        }
                        return recipes;
                    }
                });
    }

    private interface Service {
        @GET("recipes/findByIngredients")
        Observable<List<RecipeResponse>> getRecipes(
                @Query("ingredients") String ingredients,
                @QueryMap Map<String, String> options
        );
    }
}