package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Andreas on 4/8/2018.
 */
data class RecipeResponse (
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("imageType")
    val imageType: String? = null,

    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int = 0,

    @SerializedName("missedIngredientCount")
    val missedingredientCount: Int = 0,

    @SerializedName("cuisines")
    val cuisines: List<String> = listOf(),

    @SerializedName("likes")
    val likes: Int = 0
)