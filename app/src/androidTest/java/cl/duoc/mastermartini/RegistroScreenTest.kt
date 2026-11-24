package cl.duoc.mastermartini

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import cl.duoc.mastermartini.ui.screens.RegistroScreen
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun al_enviar_formulario_vacio_debe_mostrar_mensajes_de_error_visuales() {
        val mainViewModel = MainViewModel()
        val usuarioViewModel = UsuarioViewModel()

        composeRule.setContent {
            MasterMartiniTheme {
                RegistroScreen(
                    mainViewModel = mainViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }

        // Hacemos scroll hasta el botón principal y simulamos el clic sin llenar campos
        composeRule.onNodeWithText("Registrar y Ver Resumen")
            .performScrollTo()
            .performClick()

        // Verificamos retroalimentación visual de validación
        composeRule.onNodeWithText("El nombre completo es obligatorio")
            .performScrollTo()
            .assertIsDisplayed()

        composeRule.onNodeWithText("Correo inválido (debe incluir @ y dominio)")
            .performScrollTo()
            .assertIsDisplayed()
    }
}