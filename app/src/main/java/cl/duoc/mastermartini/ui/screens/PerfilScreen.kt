package cl.duoc.mastermartini.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.ImagenInteligente
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.ui.utils.ImageUriUtils
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel

// Guía 13 Parte 4: Pantalla de Perfil integrando ImagenInteligente, Galería y Cámara
@Composable
fun PerfilScreen(
    viewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current
    val estadoUsuario by usuarioViewModel.estado.collectAsState()
    val imagenUri by usuarioViewModel.imagenPerfilUri.collectAsState()

    // URI temporal para almacenar la foto tomada por la cámara antes de confirmarla
    var uriTemporalCamara by remember { mutableStateOf<Uri?>(null) }

    // 1. Lanzador Nativo Galería: ActivityResultContracts.GetContent()
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionada: Uri? ->
        // Si el usuario cancela, uriSeleccionada es null y no crashea
        if (uriSeleccionada != null) {
            usuarioViewModel.actualizarImagenGaleria(uriSeleccionada)
        }
    }

    // 2. Lanzador Nativo Cámara: ActivityResultContracts.TakePicture()
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotoCapturadaExitosamente: Boolean ->
        // Si acepta la foto, actualizamos el StateFlow en el ViewModel; si cancela, no falla
        if (fotoCapturadaExitosamente && uriTemporalCamara != null) {
            usuarioViewModel.actualizarImagenCamara(uriTemporalCamara)
        }
    }

    // 3. Lanzador de permiso de Cámara en tiempo de ejecución
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) {
            val nuevoUri = ImageUriUtils.crearUriTemporalCamara(context)
            uriTemporalCamara = nuevoUri
            camaraLauncher.launch(nuevoUri)
        } else {
            Toast.makeText(
                context,
                "Se requiere permiso de cámara para capturar la fotografía.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Guía 13 Parte 4: Componente ImagenInteligente al centro de la pantalla
            ImagenInteligente(
                imagenUri = imagenUri,
                tamano = 150.dp,
                descripcion = "Fotografía del cliente o local gastronómico"
            )

            Text(
                text = if (estadoUsuario.nombre.isNotBlank()) {
                    "Hola, ${estadoUsuario.nombre}"
                } else {
                    "Fotografía de Perfil / Mi Negocio"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Guía 13 Parte 4: Dos botones para Galería y Cámara
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { galeriaLauncher.launch("image/*") },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Abrir Galería")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = {
                        val tienePermiso = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (tienePermiso) {
                            val nuevoUri = ImageUriUtils.crearUriTemporalCamara(context)
                            uriTemporalCamara = nuevoUri
                            camaraLauncher.launch(nuevoUri)
                        } else {
                            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Activar Cámara")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cámara", style = MaterialTheme.typography.labelLarge)
                }
            }

            HorizontalDivider()

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
                            text = "Ficha Comercial Master Martini",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Captura una foto de tu local o especialidad pastelera y completa tus datos comerciales para recibir asesoría e invitaciones.",
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