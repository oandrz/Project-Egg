package starbright.com.projectegg.data

import io.reactivex.Observable
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.data.local.model.Recipe

/**
 * Created by Andreas on 4/8/2018.
 */

interface AppDataStore {
    fun getRecipes(ingredients: String): Observable<List<Recipe>>
    fun searchIngredient(query: String): Observable<List<Ingredient>>

    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun saveDetailInformation(recipe: Recipe)
}
