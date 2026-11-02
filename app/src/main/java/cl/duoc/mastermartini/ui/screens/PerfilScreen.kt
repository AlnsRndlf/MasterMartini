package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel

@Composable
fun PerfilScreen(
    viewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val estadoUsuario by usuarioViewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Mi Perfil Comercial",
                mostrarBotonAtras = true,
                onBackClick = { viewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Profile.route,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icono de Perfil",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(84.dp)
            )

            Text(
                text = if (estadoUsuario.nombre.isNotBlank()) {
                    "Hola, ${estadoUsuario.nombre}"
                } else {
                    "¡Bienvenido al Perfil de Cliente!"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (estadoUsuario.registroCompletado) {
                        Text(
                            text = "${estadoUsuario.tipoPerfil} - ${estadoUsuario.tipoNegocio}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("RUT Empresa: ${estadoUsuario.rutEmpresa}", style = MaterialTheme.typography.bodyLarge)
                        Text("Correo: ${estadoUsuario.correo}", style = MaterialTheme.typography.bodyLarge)
                        Text("Teléfono: ${estadoUsuario.telefono}", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Intereses: ${estadoUsuario.preferenciasContenido.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "Ficha Comercial Sin Completar",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Registra los datos de tu panadería, pastelería o chocolatería y tus preferencias de contenido técnico.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.navigateTo(Screen.Registro) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.EditNote, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    if (estadoUsuario.registroCompletado) "Editar Datos de Mi Negocio"
                    else "Completar Formulario de Registro"
                )
            }

            if (estadoUsuario.registroCompletado) {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.ResumenRegistro) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Ver Resumen Completo de Registro")
                }
            }

            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.Settings) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Ir a Configuración y Accesibilidad")
            }
        }
    }
}