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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.viewmodel.EstadoViewModel
import cl.duoc.mastermartini.viewmodel.MainViewModel

// Guía 12 Partes 1, 3, 4 y 5: Pantalla interactiva con DataStore, CircularProgressIndicator y Animaciones
@Composable
fun SettingsScreen(
    viewModel: MainViewModel = viewModel(),
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val estado = estadoViewModel.activo.collectAsState()
    val mostrarMensaje = estadoViewModel.mostrarMensaje.collectAsState()
    val altoContraste by estadoViewModel.altoContraste.collectAsState()

    // Guía 12 Parte 1: Control de detalle expandible con rememberSaveable
    var mostrarDetalleAccesibilidad by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Ajustes y Accesibilidad",
                mostrarBotonAtras = true,
                onBackClick = { viewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Settings.route,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        // Guía 12 Parte 3: Simulación de carga inicial usando CircularProgressIndicator
        if (estado.value == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando preferencias desde DataStore...",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } else {
            val estaActivo = estado.value!!

            // Guía 12 Parte 1: Animación de color según el estado con tween(durationMillis = 500)
            val colorAnimado by animateColorAsState(
                targetValue = if (estaActivo) Color(0xFF1E6F30) else Color(0xFF8B1A2B),
                animationSpec = tween(durationMillis = 500),
                label = "ColorBotonModoEspecial"
            )

            // Guía 12 Parte 1: Texto derivado con derivedStateOf
            val textoBoton by remember(key1 = estaActivo) {
                derivedStateOf {
                    if (estaActivo) "Desactivar Modo Especial y Alertas"
                    else "Activar Modo Especial y Alertas"
                }
            }

            val resumenEstado by remember(key1 = estaActivo) {
                derivedStateOf {
                    if (estaActivo) {
                        "ACTIVO: Recibirás avisos de nuevos cursos técnicos y vista simplificada para mayores de 50 años."
                    } else {
                        "INACTIVO: Presiona el botón inferior para activar las alertas y la asistencia visual persistente."
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(all = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = "Modo Especial Master Martini",
                    tint = colorAnimado,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = "Modo Especial Master Martini",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                // Tarjeta con tamaño de contenido animado (animateContentSize)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(durationMillis = 400)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = resumenEstado,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resaltado extra en tarjetas",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = altoContraste,
                                onCheckedChange = { estadoViewModel.alternarAltoContraste() }
                            )
                        }

                        OutlinedButton(
                            onClick = { mostrarDetalleAccesibilidad = !mostrarDetalleAccesibilidad },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (mostrarDetalleAccesibilidad) "Ocultar detalles de accesibilidad"
                                else "Ver detalles de accesibilidad (> 50 años)"
                            )
                        }

                        if (mostrarDetalleAccesibilidad) {
                            Text(
                                text = "• Botones principales de 60dp de altura.\n• Navegación directa sin gestos complejos.\n• Persistencia automática en DataStore Preferences.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Botón principal controlado con colorAnimado y textoBoton (Guía 12 Parte 4)
                Button(
                    onClick = { estadoViewModel.alternarEstado() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAnimado),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 60.dp)
                ) {
                    Text(
                        text = textoBoton,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }

                // Guía 12 Parte 4: Feedback animado con AnimatedVisibility
                AnimatedVisibility(
                    visible = mostrarMensaje.value,
                    enter = fadeIn(tween(300)) + expandVertically(),
                    exit = fadeOut(tween(300)) + shrinkVertically()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Éxito",
                                tint = Color(0xFF1E6F30),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "¡Estado guardado exitosamente!",
                                color = Color(0xFF1E6F30),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Volver al Inicio")
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.Profile) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Ir a Mi Perfil")
                }
            }
        }
    }
}