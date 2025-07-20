package starbright.com.projectegg.compose.navigation

/**
 * Navigation destinations for Chefnut app
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Search : Screen("search")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object Favorites : Screen("favorites")
}

/**
 * Bottom navigation items
 */
enum class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: String // We'll use icon names here and map them to actual icons later
) {
    HOME(Screen.Home, "Home", "home"),
    FAVORITES(Screen.Favorites, "Favourite", "favorite")
} 