
package starbright.com.projectegg.data;

import java.util.List;

import io.reactivex.Observable;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.data.local.model.Recipe;

public interface AppDataStore {
    Observable<List<Recipe>> getRecipes(String ingredients);
    Observable<List<Ingredient>> searchIngredient(String query);

    Observable<Recipe> getRecipeDetailInformation(String recipeId);

    void saveDetailInformation(Recipe recipe);
}
