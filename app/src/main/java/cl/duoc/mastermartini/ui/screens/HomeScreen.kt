package cl.duoc.mastermartini.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.ui.utils.obtenerWindowSizeClass

// Guía 9: me siento victima de buling xparte de las guias
@Composable
fun HomeScreen(
    onIrACatalogo: () -> Unit = {},
    onIrARegistro: () -> Unit = {},
    onIrASubirContenido: () -> Unit = {}
) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            onIrACatalogo = onIrACatalogo,
            onIrARegistro = onIrARegistro,
            onIrASubirContenido = onIrASubirContenido
        )
        WindowWidthSizeClass.Medium -> HomeScreenMediana(
            onIrACatalogo = onIrACatalogo,
            onIrARegistro = onIrARegistro,
            onIrASubirContenido = onIrASubirContenido
        )
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
            onIrACatalogo = onIrACatalogo,
            onIrARegistro = onIrARegistro,
            onIrASubirContenido = onIrASubirContenido
        )
        else -> HomeScreenCompacta(
            onIrACatalogo = onIrACatalogo,
            onIrARegistro = onIrARegistro,
            onIrASubirContenido = onIrASubirContenido
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MasterMartiniTheme {
        HomeScreenCompacta()
    }
}