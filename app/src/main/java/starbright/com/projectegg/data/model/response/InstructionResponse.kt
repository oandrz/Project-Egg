/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class InstructionResponse (
    @SerializedName("number")
    val number: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("steps")
    val stepResponse: List<StepResponse>
)