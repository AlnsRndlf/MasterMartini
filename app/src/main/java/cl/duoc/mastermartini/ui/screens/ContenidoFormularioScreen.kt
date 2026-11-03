package cl.duoc.mastermartini.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.ImagenInteligente
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.ui.utils.ImageUriUtils
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import cl.duoc.mastermartini.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContenidoFormularioScreen(
    mainViewModel: MainViewModel,
    contenidoViewModel: ContenidoViewModel
) {
    val context = LocalContext.current
    val formState by contenidoViewModel.formState.collectAsState()
    val esEdicion = formState.idEdicion != null

    var uriTemporalReceta by remember { mutableStateOf<Uri?>(null) }

    val galeriaRecetaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            contenidoViewModel.onImagenUriChange(uri.toString())
        }
    }

    val camaraRecetaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito: Boolean ->
        if (exito && uriTemporalReceta != null) {
            contenidoViewModel.onImagenUriChange(uriTemporalReceta.toString())
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) {
            val nuevoUri = ImageUriUtils.crearUriTemporalCamara(context)
            uriTemporalReceta = nuevoUri
            camaraRecetaLauncher.launch(nuevoUri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val tiposContenido = listOf("Receta", "Curso", "Video Técnico")
    val categoriasEspecialidad = listOf(
        "Chocolatería",
        "Pastelería de vitrina",
        "Heladería",
        "Bollería",
        "Masas de hoja",
        "Galletas",
        "Empanadas",
        "Productos de horneo"
    )

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = if (esEdicion) "Editar Contenido Técnico" else "Subir Receta o Curso",
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.FormularioContenido.route,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (esEdicion) "Modificar Ficha Técnica" else "Nueva Publicación Técnica",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Fotografía del plato o curso usando ImagenInteligente (Guía 13)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImagenInteligente(
                    imagenUri = formState.imagenUri?.let { Uri.parse(it) },
                    tamano = 120.dp,
                    descripcion = "Foto del producto elaborado"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { galeriaRecetaLauncher.launch("image/*") },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Foto Galería")
                    }
                    OutlinedButton(
                        onClick = {
                            val tienePermiso = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                            if (tienePermiso) {
                                val nuevoUri = ImageUriUtils.crearUriTemporalCamara(context)
                                uriTemporalReceta = nuevoUri
                                camaraRecetaLauncher.launch(nuevoUri)
                            } else {
                                permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Foto Cámara")
                    }
                }
            }

            // 1. Selección de Tipo (Receta, Curso, Video Técnico)
            Text("1. Tipo de Contenido", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                tiposContenido.forEach { tipo ->
                    val seleccionado = formState.tipo == tipo
                    FilterChip(
                        selected = seleccionado,
                        onClick = { contenidoViewModel.onTipoChange(tipo) },
                        label = { Text(tipo, style = MaterialTheme.typography.bodyLarge) },
                        leadingIcon = if (seleccionado) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }

            // 2. Selección de Especialidad
            Text("2. Especialidad Gastronómica", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoriasEspecialidad.forEach { cat ->
                    val seleccionada = formState.categoria == cat
                    FilterChip(
                        selected = seleccionada,
                        onClick = { contenidoViewModel.onCategoriaChange(cat) },
                        label = { Text(cat, style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = if (seleccionada) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }

            // 3. Campo Título
            OutlinedTextField(
                value = formState.titulo,
                onValueChange = contenidoViewModel::onTituloChange,
                label = { Text("Título de la receta, curso o video") },
                leadingIcon = { Icon(Icons.Default.Title, contentDescription = "Título") },
                trailingIcon = {
                    if (formState.errores.titulo != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en título",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = formState.errores.titulo != null,
                supportingText = {
                    formState.errores.titulo?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 4. Campo Insumo Master Martini
            OutlinedTextField(
                value = formState.insumoMartini,
                onValueChange = contenidoViewModel::onInsumoChange,
                label = { Text("Insumo Master Martini (Ej: Chocolate Ariba, Decor Up)") },
                leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = "Insumo") },
                trailingIcon = {
                    if (formState.errores.insumoMartini != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en insumo",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = formState.errores.insumoMartini != null,
                supportingText = {
                    formState.errores.insumoMartini?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 5. Campo Duración
            OutlinedTextField(
                value = formState.duracion,
                onValueChange = contenidoViewModel::onDuracionChange,
                label = { Text("Duración estimada (Ej: 45 min o 4 horas)") },
                leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = "Duración") },
                trailingIcon = {
                    if (formState.errores.duracion != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en duración",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = formState.errores.duracion != null,
                supportingText = {
                    formState.errores.duracion?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 6. Campo Pasos Técnicos / Temario
            OutlinedTextField(
                value = formState.pasos,
                onValueChange = contenidoViewModel::onPasosChange,
                label = { Text("Pasos de elaboración o temario técnico") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = "Pasos") },
                trailingIcon = {
                    if (formState.errores.pasos != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error en pasos",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = formState.errores.pasos != null,
                supportingText = {
                    formState.errores.pasos?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Button(
                onClick = {
                    if (contenidoViewModel.validarYGuardarContenido()) {
                        mainViewModel.navigateTo(Screen.Catalogo)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (esEdicion) "Guardar Cambios" else "Publicar en el Catálogo",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}