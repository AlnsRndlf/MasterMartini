package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.ui.utils.obtenerWindowSizeClass
import cl.duoc.mastermartini.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val windowSizeClass = obtenerWindowSizeClass()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Master Martini",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Inicio (Actual)") },
                    selected = true,
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Catálogo de Recetas y Cursos") },
                    selected = false,
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Catalogo)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Subir Receta / Contenido") },
                    selected = false,
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.FormularioContenido)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Registro de Cliente / Prospecto") },
                    selected = false,
                    icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Registro)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Mi Perfil") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Configuración y Accesibilidad") },
                    selected = false,
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                MasterMartiniTopBar(
                    title = "Master Martini - Inicio",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                MasterMartiniBottomBar(
                    currentRoute = Screen.Home.route,
                    onNavigate = { destino -> viewModel.navigateTo(destino) }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                val irACatalogo = { viewModel.navigateTo(Screen.Catalogo) }
                val irARegistro = { viewModel.navigateTo(Screen.Registro) }
                val irASubir = { viewModel.navigateTo(Screen.FormularioContenido) }

                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> HomeScreenCompacta(
                        onIrACatalogo = irACatalogo,
                        onIrARegistro = irARegistro,
                        onIrASubirContenido = irASubir
                    )
                    WindowWidthSizeClass.Medium -> HomeScreenMediana(
                        onIrACatalogo = irACatalogo,
                        onIrARegistro = irARegistro,
                        onIrASubirContenido = irASubir
                    )
                    WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
                        onIrACatalogo = irACatalogo,
                        onIrARegistro = irARegistro,
                        onIrASubirContenido = irASubir
                    )
                    else -> HomeScreenCompacta(
                        onIrACatalogo = irACatalogo,
                        onIrARegistro = irARegistro,
                        onIrASubirContenido = irASubir
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MasterMartiniTheme {
        HomeScreenCompacta()
    }
}