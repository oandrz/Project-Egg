package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @SerializedName("results")
    val results: List<RecipeResponse>
)