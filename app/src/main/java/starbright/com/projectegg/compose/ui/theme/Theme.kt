package starbright.com.projectegg.compose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ChefnutOrange,
    onPrimary = ChefnutWhite,
    primaryContainer = ChefnutOrangeDark,
    onPrimaryContainer = ChefnutWhite,
    secondary = ChefnutGreen,
    onSecondary = ChefnutWhite,
    secondaryContainer = ChefnutGreenDark,
    onSecondaryContainer = ChefnutWhite,
    tertiary = ChefnutBrown,
    onTertiary = ChefnutWhite,
    tertiaryContainer = ChefnutBrownDark,
    onTertiaryContainer = ChefnutWhite,
    error = ChefnutError,
    onError = ChefnutWhite,
    errorContainer = ChefnutRedDark,
    onErrorContainer = ChefnutWhite,
    background = ChefnutBlack,
    onBackground = ChefnutWhite,
    surface = ChefnutDarkGray,
    onSurface = ChefnutWhite,
    surfaceVariant = ChefnutDarkGray,
    onSurfaceVariant = ChefnutGray,
    outline = ChefnutGray,
    outlineVariant = ChefnutDarkGray,
    scrim = ChefnutBlack
)

private val LightColorScheme = lightColorScheme(
    primary = ChefnutRed,
    onPrimary = ChefnutWhite,
    primaryContainer = ChefnutOrange,
    onPrimaryContainer = ChefnutWhite,
    secondary = ChefnutGreen,
    onSecondary = ChefnutWhite,
    secondaryContainer = ChefnutGreen.copy(alpha = 0.1f),
    onSecondaryContainer = ChefnutGreenDark,
    tertiary = ChefnutBrown,
    onTertiary = ChefnutWhite,
    tertiaryContainer = ChefnutBrown.copy(alpha = 0.1f),
    onTertiaryContainer = ChefnutBrownDark,
    error = ChefnutError,
    onError = ChefnutWhite,
    errorContainer = ChefnutError.copy(alpha = 0.1f),
    onErrorContainer = ChefnutRedDark,
    background = ChefnutBackground,
    onBackground = ChefnutBlack,
    surface = ChefnutSurface,
    onSurface = ChefnutBlack,
    surfaceVariant = ChefnutSurfaceVariant,
    onSurfaceVariant = ChefnutDarkGray,
    outline = ChefnutGray,
    outlineVariant = ChefnutLightGray,
    scrim = ChefnutBlack.copy(alpha = 0.32f)
)

@Composable
fun ChefnutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
} 