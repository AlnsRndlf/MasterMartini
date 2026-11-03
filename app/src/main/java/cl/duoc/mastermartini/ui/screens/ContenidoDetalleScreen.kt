package cl.duoc.mastermartini.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import cl.duoc.mastermartini.viewmodel.MainViewModel

@Composable
fun ContenidoDetalleScreen(
    contenidoId: Int,
    mainViewModel: MainViewModel,
    contenidoViewModel: ContenidoViewModel
) {
    val lista by contenidoViewModel.contenidosFiltrados.collectAsState()
    val contenido = lista.find { it.id == contenidoId }

    // Estado interactivo con rememberSaveable y animación para inscripción/guardado
    var inscritoOGuardado by rememberSaveable(contenidoId) { mutableStateOf(false) }

    val colorInscripcion by animateColorAsState(
        targetValue = if (inscritoOGuardado) Color(0xFF1E6F30) else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(durationMillis = 500),
        label = "ColorBotonInscripcion"
    )

    val textoAccion by remember(inscritoOGuardado, contenido?.tipo) {
        derivedStateOf {
            when {
                inscritoOGuardado && contenido?.tipo == "Curso" -> "Inscrito en el Curso (Cancelar)"
                inscritoOGuardado -> "Guardado en Mi Recetario (Quitar)"
                contenido?.tipo == "Curso" -> "Inscribirse al Curso Técnico"
                else -> "Guardar en Mi Recetario"
            }
        }
    }

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = contenido?.tipo ?: "Detalle Técnico",
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Catalogo.route,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (contenido == null) {
                Text(
                    text = "El contenido seleccionado ya no se encuentra disponible.",
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                Text(
                    text = contenido.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(durationMillis = 350)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Categoría: ${contenido.categoria}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Duración estimada: ${contenido.duracion}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Insumo Master Martini: ${contenido.insumoMartini}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider()
                        Text(
                            text = "Instrucciones Paso a Paso / Temario:",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = contenido.pasos,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Botón animado de inscripción o guardado con feedback visual (Guía 12)
                Button(
                    onClick = { inscritoOGuardado = !inscritoOGuardado },
                    colors = ButtonDefaults.buttonColors(containerColor = colorInscripcion),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Icon(Icons.Default.BookmarkAdded, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = textoAccion,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }

                AnimatedVisibility(
                    visible = inscritoOGuardado,
                    enter = fadeIn(tween(300)) + expandVertically(),
                    exit = fadeOut(tween(300)) + shrinkVertically()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Confirmado",
                                tint = Color(0xFF1E6F30),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (contenido.tipo == "Curso") {
                                    "¡Inscripción registrada! El equipo de Master Martini te enviará los detalles."
                                } else {
                                    "¡Contenido destacado en tu recetario personal!"
                                },
                                color = Color(0xFF1E6F30),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            contenidoViewModel.cargarParaEditar(contenido.id)
                            mainViewModel.navigateTo(Screen.EditarContenido(contenido.id.toString()))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar")
                    }

                    Button(
                        onClick = {
                            contenidoViewModel.eliminarContenido(contenido)
                            mainViewModel.navigateBack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar")
                    }
                }
            }

            OutlinedButton(
                onClick = { mainViewModel.navigateBack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Catálogo")
            }
        }
    }
}