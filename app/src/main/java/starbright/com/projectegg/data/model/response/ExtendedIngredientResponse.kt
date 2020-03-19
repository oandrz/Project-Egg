/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class ExtendedIngredientResponse (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("amount")
    val amount: Float = 0f,

    @SerializedName("unit")
    val unit: String? = null,

    @SerializedName("image")
    val image: String? = null
)