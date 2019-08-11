/**
 * Created by Andreas on 7/10/2018.
 */

package starbright.com.projectegg.data

import io.reactivex.Observable
import starbright.com.projectegg.data.local.model.Ingredient
import starbright.com.projectegg.data.local.model.Recipe

interface AppDataStore {
    fun getRecipes(ingredients: String): Observable<List<Recipe>>
    fun searchIngredient(query: String): Observable<List<Ingredient>>

    fun getRecipeDetailInformation(recipeId: String): Observable<Recipe>

    fun saveDetailInformation(recipe: Recipe)
}
