package starbright.com.projectegg.compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme

@Composable
fun ChefnutBottomNavigation(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        BottomNavItem.values().forEach { item ->
            val selected = currentRoute == item.screen.route
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        onItemClick(item.screen.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavItem.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
                            BottomNavItem.FAVORITES -> if (selected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                        },
                        contentDescription = "${item.label} icon",
                        modifier = Modifier.semantics {
                            contentDescription = "${item.label} icon"
                        }
                    )
                },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChefnutBottomNavigationPreview() {
    ChefnutTheme {
        ChefnutBottomNavigation(
            currentRoute = BottomNavItem.HOME.screen.route,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChefnutBottomNavigationFavoritesSelectedPreview() {
    ChefnutTheme {
        ChefnutBottomNavigation(
            currentRoute = BottomNavItem.FAVORITES.screen.route,
            onItemClick = {}
        )
    }
} 