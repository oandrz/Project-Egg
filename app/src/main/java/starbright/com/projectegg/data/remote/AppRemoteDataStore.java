/**
 * Created by Andreas on 7/10/2018.
 */

/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import starbright.com.projectegg.dagger.qualifier.RemoteDataStore;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.data.local.model.response.IngredientResponse;
import starbright.com.projectegg.data.local.model.response.RecipeDetailResponse;
import starbright.com.projectegg.data.local.model.response.RecipeResponse;
import starbright.com.projectegg.util.Constants;

/**
 * Created by Andreas on 4/8/2018.
 */

@Singleton
public class AppRemoteDataStore implements AppDataStore {

    private Retrofit mRetrofit;

    @Inject
    AppRemoteDataStore(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    @Override
    public Observable<List<Recipe>> getRecipes(String ingredients) {
        return mRetrofit.create(Service.class).getRecipes(ingredients, new HashMap<String, String>())
                .map(new Function<List<RecipeResponse>, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(List<RecipeResponse> responses) {
                        final List<Recipe> recipes = new ArrayList<>(responses.size());
                        for (RecipeResponse response : responses) {
                            recipes.add(new Recipe(response));
                        }
                        return recipes;
                    }
                });
    }

    @Override
    public Observable<List<Ingredient>> searchIngredient(String query) {
        final int numberOfIngredient = 5;
        final Map<String, String> queryMap = new HashMap<>();
        queryMap.put(Constants.QUERY_PARAM_META_INFORMATION_KEY, String.valueOf(true));
        queryMap.put(Constants.QUERY_PARAM_NUMBER_KEY, String.valueOf(numberOfIngredient));
        return mRetrofit.create(Service.class).searchAutocompleteIngredients(query, queryMap)
                .map(new Function<List<IngredientResponse>, List<Ingredient>>() {
                    @Override
                    public List<Ingredient> apply(List<IngredientResponse> responses) {
                        final List<Ingredient> ingredients = new ArrayList<>(responses.size());
                        for (IngredientResponse response : responses) {
                            ingredients.add(new Ingredient(response));
                        }
                        return ingredients;
                    }
                });
    }

    @Override
    public Observable<Recipe> getRecipeDetailInformation(String recipeId) {
        return mRetrofit.create(Service.class).getRecipeDetailInformation(recipeId)
                .map(new Function<RecipeDetailResponse, Recipe>() {
                    @Override
                    public Recipe apply(RecipeDetailResponse recipeDetailResponse) {
                        return new Recipe(recipeDetailResponse);
                    }
                });
    }

    @Override
    public void saveDetailInformation(Recipe recipe) {
        throw new UnsupportedOperationException();
    }

    private interface Service {
        @GET("recipes/findByIngredients")
        Observable<List<RecipeResponse>> getRecipes(
                @Query("ingredients") String ingredients,
                @QueryMap Map<String, String> options
        );

        @GET("food/ingredients/autocomplete")
        Observable<List<IngredientResponse>> searchAutocompleteIngredients(
                @Query("query") String query,
                @QueryMap Map<String, String> options
        );

        @GET("recipes/{recipeId}/information")
        Observable<RecipeDetailResponse> getRecipeDetailInformation(
                @Path("recipeId") String recipeId
        );
    }
}