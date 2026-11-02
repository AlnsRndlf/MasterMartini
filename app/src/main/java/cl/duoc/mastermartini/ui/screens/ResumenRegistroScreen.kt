package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel


// resumen del registro
@Composable
fun ResumenRegistroScreen(
    mainViewModel: MainViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val estado by usuarioViewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Resumen del Registro",
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Profile.route,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Registro Exitoso",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Resumen del Registro",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Perfil: ${estado.tipoPerfil}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()
                    Text(text = "Nombre: ${estado.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "RUT Empresa: ${estado.rutEmpresa}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Correo: ${estado.correo}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Teléfono: ${estado.telefono}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Dirección: ${estado.direccion}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Contraseña: ${"*".repeat(estado.clave.length)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    HorizontalDivider()
                    Text(
                        text = "Rubro Gastronómico: ${estado.tipoNegocio}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Tamaño del Negocio: ${estado.tamanoNegocio}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Especialidades de Interés: ${
                            if (estado.preferenciasContenido.isEmpty()) "Ninguna"
                            else estado.preferenciasContenido.joinToString(", ")
                        }",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Button(
                onClick = { mainViewModel.navigateTo(Screen.Catalogo) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Ir al Catálogo de Contenidos")
            }

            OutlinedButton(
                onClick = { mainViewModel.navigateTo(Screen.Registro) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Modificar Datos del Formulario")
            }
        }
    }
}