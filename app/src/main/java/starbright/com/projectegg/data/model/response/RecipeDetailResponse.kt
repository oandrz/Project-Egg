/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse (
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String,

    @SerializedName("image")
    val imageStringUrl: String? = null,

    @SerializedName("readyInMinutes")
    val cookingTime: Int = 0,

    @SerializedName("sourceUrl")
    val sourceStringUrl: String? = null,

    @SerializedName("sourceName")
    val sourceName: String? = null,

    @SerializedName("servings")
    val servings: Int = 0,

    @SerializedName("nutrition")
    val nutrients: NutritionResponse,

    @SerializedName("dishTypes")
    val dishTypes: List<String>,

    @SerializedName("cuisines")
    val cuisines: List<String>,

    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<InstructionResponse>,

    @SerializedName("extendedIngredients")
    val extendedIngredientResponse: List<ExtendedIngredientResponse>
)