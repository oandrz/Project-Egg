/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 15/8/2018.
 */
package starbright.com.projectegg.data.model.response

import com.google.gson.annotations.SerializedName

data class StepResponse(
    @SerializedName("number")
    val number: Int,

    @SerializedName("step")
    val step: String,

    @SerializedName("ingredients")
    val ingredients: List<IngredientResponse>
)