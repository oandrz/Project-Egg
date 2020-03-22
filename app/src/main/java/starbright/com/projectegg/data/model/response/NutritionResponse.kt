package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class NutritionResponse (
    @SerializedName("nutrients")
    var nutrients: List<NutrientResponse>
)