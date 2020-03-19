/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse (
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("image")
    val imageStringUrl: String? = null,

    @SerializedName("readyInMinutes")
    val preparationMinutes: Int = 0,

    @SerializedName("cookingMinutes")
    val cookingMinutes: Int = 0,

    @SerializedName("sourceUrl")
    val sourceStringUrl: String? = null,

    @SerializedName("sourceName")
    val sourceName: String? = null,

    @SerializedName("servings")
    val servings: Int = 0,

    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<InstructionResponse>? = null,

    @SerializedName("extendedIngredients")
    val extendedIngredientResponse: List<ExtendedIngredientResponse>? = null
)