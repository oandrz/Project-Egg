/**
 * Created by Andreas on 29/9/2018.
 */

package starbright.com.projectegg.util

/**
 * Created by Andreas on 4/8/2018.
 */

object Constants {

    const val COMMA = ","
    const val YYYY_MM_DD_FORMAT = "yyyyMMdd"
    const val EMAIL_FORMAT = "^[a-zA-Z0-9_+&*-]+(?:\\." +
        "[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
        "A-Z]{2,7}$"
    const val JPG = ".jpg"

    const val PROVIDER_PACKAGE_NAME = "starbright.com.projectegg.fileprovider"


    //  Networking
    const val IMAGE_INGREDIENT_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    const val QUERY_PARAM_META_INFORMATION_KEY = "metaInformation"
    const val QUERY_PARAM_LIMIT_LICENSE_KEY = "limitLicense"
    const val QUERY_PARAM_INSTRUCTION_REQUIRED_KEY = "instructionsRequired"
    const val QUERY_PARAM_ADD_INFORMATION = "addRecipeInformation"
    const val QUERY_PARAM_SORT_KEY = "sort"
    const val QUERY_PARAM_NUMBER_KEY = "number"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_PARAM_INCL_NUTRITION = "includeNutrition"

    const val OPERATION_NOT_SUPPORTED = "Operation not supported"

    const val DATABASE_NAME = "db_recipe"
}
