package cl.duoc.mastermartini

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import cl.duoc.mastermartini.model.Post
import cl.duoc.mastermartini.ui.screens.PostScreen
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.viewmodel.PostViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

// Guía 15 Parte 5: Prueba de UI para PostScreen.kt con ViewModel simulado
class PostScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() {
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Contenido 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Contenido 2")
        )

        // Subclase falsa de PostViewModel con StateFlow simulado y sin llamada a red
        val fakeViewModel = object : PostViewModel(autoFetch = false) {
            override val postList = MutableStateFlow(value = fakePosts)
        }

        composeRule.setContent {
            MasterMartiniTheme {
                PostScreen(viewModel = fakeViewModel)
            }
        }

        composeRule.onNodeWithText(text = "Título: Título 1").assertIsDisplayed()
        composeRule.onNodeWithText(text = "Título: Título 2").assertIsDisplayed()
    }
}