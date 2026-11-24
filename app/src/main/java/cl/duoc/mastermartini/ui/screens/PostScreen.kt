package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.model.Post
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.PostViewModel


@Composable
fun PostScreen(
    viewModel: PostViewModel,
    onBackClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {}
) {
    val posts by viewModel.postList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()

    var mostrarDialogoNuevo by remember { mutableStateOf(false) }
    var postEnEdicion by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Comunidad (API Externa)",
                mostrarBotonAtras = true,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.ComunidadApi.route,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.limpiarMensajes()
                    mostrarDialogoNuevo = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publicar (POST)", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cabecera informativa con botón de recarga GET
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Novedades JSONPlaceholder",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(onClick = { viewModel.fetchPosts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Recargar")
                    }
                }

                // Banner de retroalimentación exitosa (POST / PUT / DELETE)
                feedbackMessage?.let { mensaje ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Operación exitosa",
                                tint = Color(0xFF1E6F30)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mensaje,
                                color = Color(0xFF1E6F30),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Banner de error con reintento
                errorMessage?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (isLoading && posts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(modifier = Modifier.size(52.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Consultando publicaciones externas...",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = posts, key = { it.id }) { post ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Publicación #${post.id}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "Título: ${post.title}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = post.body,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    HorizontalDivider()
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.limpiarMensajes()
                                                postEnEdicion = post
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Editar (PUT)")
                                        }
                                        OutlinedButton(
                                            onClick = { viewModel.eliminarPost(post.id) },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Borrar", color = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoNuevo) {
        DialogoPostFormulario(
            tituloDialogo = "Nueva Publicación (POST)",
            tituloInicial = "",
            cuerpoInicial = "",
            onDismiss = { mostrarDialogoNuevo = false },
            onConfirm = { titulo, cuerpo ->
                viewModel.crearPost(titulo, cuerpo)
                mostrarDialogoNuevo = false
            }
        )
    }

    postEnEdicion?.let { post ->
        DialogoPostFormulario(
            tituloDialogo = "Editar Publicación #${post.id} (PUT)",
            tituloInicial = post.title,
            cuerpoInicial = post.body,
            onDismiss = { postEnEdicion = null },
            onConfirm = { nuevoTitulo, nuevoCuerpo ->
                viewModel.actualizarPost(post, nuevoTitulo, nuevoCuerpo)
                postEnEdicion = null
            }
        )
    }
}

@Composable
private fun DialogoPostFormulario(
    tituloDialogo: String,
    tituloInicial: String,
    cuerpoInicial: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var titulo by remember(tituloInicial) { mutableStateOf(tituloInicial) }
    var cuerpo by remember(cuerpoInicial) { mutableStateOf(cuerpoInicial) }
    var mostrarError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = tituloDialogo, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        mostrarError = false
                    },
                    label = { Text("Título") },
                    isError = mostrarError && titulo.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = cuerpo,
                    onValueChange = {
                        cuerpo = it
                        mostrarError = false
                    },
                    label = { Text("Mensaje / Contenido") },
                    isError = mostrarError && cuerpo.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                if (mostrarError) {
                    Text(
                        text = "Complete ambos campos para enviar la solicitud.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isBlank() || cuerpo.isBlank()) {
                        mostrarError = true
                    } else {
                        onConfirm(titulo, cuerpo)
                    }
                }
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}