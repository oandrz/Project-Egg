package starbright.com.projectegg.data.remote;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
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
        return retrofit.create(Service.class).getRecipes(ingredients)
                .map(new Function<BaseResponse, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(BaseResponse baseResponse) throws Exception {
                        final List<RecipeResponse> recipesResponse = baseResponse.getmResults();
                        List<Recipe> recipes = new ArrayList<>(recipesResponse.size());
                        for (RecipeResponse response : recipesResponse) {
                            recipes.add(new Recipe(response));
                        }
                        return recipes;
                    }
                });
    }

    private interface Service {
        @GET("api/")
        Observable<BaseResponse> getRecipes(@Query("i") String ingredients);
    }
}