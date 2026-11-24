package cl.duoc.mastermartini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.AppNavigation
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.viewmodel.EstadoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val estadoViewModel: EstadoViewModel = viewModel(
                factory = EstadoViewModel.provideFactory(applicationContext)
            )
            val altoContraste by estadoViewModel.altoContraste.collectAsState()

            MasterMartiniTheme(altoContraste = altoContraste) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(estadoViewModel = estadoViewModel)
                }
            }
        }
    }
}