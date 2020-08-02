package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Andreas on 4/8/2018.
 */
data class RecipeResponse (
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("cuisines")
    val cuisines: List<String> = listOf()
)