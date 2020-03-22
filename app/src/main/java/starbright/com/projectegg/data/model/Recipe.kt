/**
 * Created by Andreas on 7/10/2018.
 *
 *
 * Created by Andreas on 29/9/2018.
 *
 *
 * Created by Andreas on 29/9/2018.
 *
 *
 * Created by Andreas on 29/9/2018.
 *
 *
 * Created by Andreas on 29/9/2018.
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