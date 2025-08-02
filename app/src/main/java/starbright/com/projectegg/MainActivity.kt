package starbright.com.projectegg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import starbright.com.projectegg.R
import starbright.com.projectegg.compose.navigation.BottomNavItem
import starbright.com.projectegg.compose.navigation.ChefnutNavHost
import starbright.com.projectegg.compose.navigation.Screen
import starbright.com.projectegg.compose.ui.screens.main.MainScreen
import starbright.com.projectegg.compose.ui.theme.ChefnutTheme
import javax.inject.Inject

/**
 * Main activity for the Chefnut app using Jetpack Compose
 * This serves as the entry point and handles navigation
 */
class MainActivity : ComponentActivity() {
    
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get ViewModel factory from Dagger
        viewModelFactory = (application as MyApp).appComponent.getViewModelFactory()
        
        setContent {
            ChefnutTheme {
                ChefnutApp(viewModelFactory)
            }
        }
    }
}

@Composable
fun ChefnutApp(viewModelFactory: ViewModelProvider.Factory) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen(viewModelFactory = viewModelFactory)
    }
} 