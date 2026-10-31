package cl.duoc.mastermartini.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MartiniRedDarkTheme,
    onPrimary = MartiniRedDark,
    secondary = MartiniGoldDarkTheme,
    background = MartiniBackgroundDark,
    surface = MartiniSurfaceDark,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFFFB4AB)
)

private val LightColorScheme = lightColorScheme(
    primary = MartiniRedPrimary,
    onPrimary = Color.White,
    primaryContainer = MartiniSurfaceVariant,
    onPrimaryContainer = MartiniRedDark,
    secondary = MartiniGoldSecondary,
    onSecondary = Color.White,
    background = MartiniCreamBackground,
    onBackground = MartiniTextPrimary,
    surface = MartiniSurfaceCard,
    onSurface = MartiniTextPrimary,
    surfaceVariant = MartiniSurfaceVariant,
    onSurfaceVariant = MartiniTextSecondary,
    outline = MartiniOutline,
    error = MartiniError,
    onError = Color.White
)

@Composable
fun MasterMartiniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}