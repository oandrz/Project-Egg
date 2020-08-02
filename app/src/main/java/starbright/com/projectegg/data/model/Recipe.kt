/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 1 - 8 - 2020.
 */

package starbright.com.projectegg.data.model

data class Recipe(
    var id: Int,
    var cookingMinutes: Int? = 0,
    var servingCount: Int? = 0,
    var title: String,
    var image: String?,
    var calories: Int? = 0,
    var sourceStringUrl: String? = "",
    var sourceName: String? = "",
    var ingredients: List<Ingredient>? = null,
    var instructions: List<Instruction>? = null,
    var dishTypes: List<String>? = null,
    var cuisines: List<String>? = null
)
