package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class NutrientResponse (
    @SerializedName("title")
    var title: String,

    @SerializedName("amount")
    var amount: Double,

    @SerializedName("unit")
    var unit: String
)