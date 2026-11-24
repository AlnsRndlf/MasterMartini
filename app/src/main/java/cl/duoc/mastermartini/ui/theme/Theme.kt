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

// Esquema de contraste máximo para el interruptor de accesibilidad (> 50 años)
private val HighContrastColorScheme = lightColorScheme(
    primary = Color(0xFF5C0011),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE5E8),
    onPrimaryContainer = Color(0xFF2B0007),
    secondary = Color(0xFF7A4400),
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE6DDD5),
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    error = Color(0xFF990000),
    onError = Color.White
)

@Composable
fun MasterMartiniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    altoContraste: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        altoContraste -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}