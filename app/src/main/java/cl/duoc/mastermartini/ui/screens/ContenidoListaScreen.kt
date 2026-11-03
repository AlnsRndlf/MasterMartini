package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.ui.components.TarjetaContenido
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import cl.duoc.mastermartini.viewmodel.MainViewModel

@Composable
fun ContenidoListaScreen(
    mainViewModel: MainViewModel,
    contenidoViewModel: ContenidoViewModel
) {
    val listaContenidos by contenidoViewModel.contenidosFiltrados.collectAsState()
    val tipoSeleccionado by contenidoViewModel.filtroTipo.collectAsState()

    val tipos = listOf("Todos", "Receta", "Curso", "Video Técnico")

    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = "Catálogo Técnico",
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = Screen.Catalogo.route,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    contenidoViewModel.prepararNuevoFormulario()
                    mainViewModel.navigateTo(Screen.FormularioContenido)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Receta o Curso")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Subir Contenido", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Filtros superiores claros y grandes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tipos.forEach { tipo ->
                    val seleccionado = tipoSeleccionado == tipo
                    FilterChip(
                        selected = seleccionado,
                        onClick = { contenidoViewModel.seleccionarFiltroTipo(tipo) },
                        label = {
                            Text(
                                text = tipo,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        leadingIcon = if (seleccionado) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listaContenidos, key = { it.id }) { item ->
                    TarjetaContenido(
                        contenido = item,
                        onVerDetalle = {
                            mainViewModel.navigateTo(Screen.Detail(item.id.toString()))
                        },
                        onEditar = {
                            contenidoViewModel.cargarParaEditar(item.id)
                            mainViewModel.navigateTo(Screen.EditarContenido(item.id.toString()))
                        },
                        onEliminar = {
                            contenidoViewModel.eliminarContenido(item)
                        }
                    )
                }
            }
        }
    }
}