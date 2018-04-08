package starbright.com.projectegg.features.recipelist;

/**
 * Created by Andreas on 4/8/2018.
 */

public class RecipeListContract {

    interface View {

    }

    interface Presenter {
        void getRecipesBasedIngredients(String ingredients);
    }
}
