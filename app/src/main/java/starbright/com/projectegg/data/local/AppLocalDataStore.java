/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.data.local;

import java.util.List;

import io.reactivex.Observable;
import starbright.com.projectegg.data.AppDataStore;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;
import starbright.com.projectegg.util.Constants;

/**
 * Created by Andreas on 4/8/2018.
 */

public class AppLocalDataStore implements AppDataStore {

    @Override
    public Observable<List<Recipe>> getRecipes(String ingredients) {
        System.err.print(Constants.OPERATION_NOT_SUPPORTED);
        return null;
    }

    @Override
    public Observable<List<Ingredient>> searchIngredient(String query) {
        System.err.println(Constants.OPERATION_NOT_SUPPORTED);
        return null;
    }

    @Override
    public Observable<Recipe> getRecipeDetailInformation(String recipeId) {
        return null;
    }
}
