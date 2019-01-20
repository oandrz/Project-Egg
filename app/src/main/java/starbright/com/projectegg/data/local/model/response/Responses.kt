/**
 * Created by Andreas on 20/1/2019.
 */

package starbright.com.projectegg.data.local.model.response

import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse(
        @SerializedName("results") val recipes: List<RecipeResponse>,
        @SerializedName("baseUri") val imagePath: String
)

data class RecipeResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("image") val image: String,
        @SerializedName("imageType") val imageType: String,
        @SerializedName("usedIngredientCount") val usedIngredientsCount: Int,
        @SerializedName("missedIngredientCount") val missedIngredientsCount: Int,
        @SerializedName("likes") val likes: Int
)

data class InstructionResponse(
        @SerializedName("name") val name: String,
        @SerializedName("steps") val steps: List<StepResponse>
)

data class RecipeDetailResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("image") val image: String,
        @SerializedName("preparationMinutes") val preparationMinute: Int,
        @SerializedName("cookingMinutes") val cookingMinutes: Int,
        @SerializedName("sourceUrl") val sourceStringUrl: String,
        @SerializedName("sourceName") val sourceName: String,
        @SerializedName("servings") val servings: Int,
        @SerializedName("analyzedInstructions") val analyzedInstructions: List<InstructionResponse>,
        @SerializedName("extendedIngredients") val extendedIngredientResponse: List<ExtendedIngredientResponse>
)

data class IngredientResponse(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("image") val imagePath: String
)

data class ExtendedIngredientResponse(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("amount") val amount: Float,
        @SerializedName("unit") val unit: String,
        @SerializedName("image") val image: String
)

data class StepResponse(
        @SerializedName("number") val number: String,
        @SerializedName("step") val step: String,
        @SerializedName("ingredients") val ingredients: List<IngredientResponse>
)