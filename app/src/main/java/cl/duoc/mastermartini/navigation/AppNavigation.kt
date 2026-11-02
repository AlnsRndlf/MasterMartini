package cl.duoc.mastermartini.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mastermartini.ui.components.MasterMartiniBottomBar
import cl.duoc.mastermartini.ui.components.MasterMartiniTopBar
import cl.duoc.mastermartini.ui.screens.HomeScreen
import cl.duoc.mastermartini.ui.screens.PerfilScreen
import cl.duoc.mastermartini.ui.screens.RegistroScreen
import cl.duoc.mastermartini.ui.screens.ResumenRegistroScreen
import cl.duoc.mastermartini.ui.screens.SettingsScreen
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

// grafo de navegacion centralizado observando los eventos del MainViewModel
@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(route = event.route.route) {
                        event.popUpToRoute?.let { popRoute ->
                            popUpTo(route = popRoute.route) {
                                inclusive = event.inclusive
                            }
                        }
                        launchSingleTop = event.singleTop
                        restoreState = true
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(viewModel = mainViewModel)
        }

        composable(route = Screen.Profile.route) {
            PerfilScreen(
                viewModel = mainViewModel,
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(viewModel = mainViewModel)
        }

        composable(route = Screen.Registro.route) {
            RegistroScreen(
                mainViewModel = mainViewModel,
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(route = Screen.ResumenRegistro.route) {
            ResumenRegistroScreen(
                mainViewModel = mainViewModel,
                usuarioViewModel = usuarioViewModel
            )
        }

        composable(route = Screen.Catalogo.route) {
            PantallaBaseNavegable(
                titulo = Screen.Catalogo.title,
                rutaActual = Screen.Catalogo.route,
                descripcion = "Catálogo de Recetas, Cursos y Videos Técnicos Master Martini.",
                mainViewModel = mainViewModel
            )
        }

        composable(route = Screen.FormularioContenido.route) {
            PantallaBaseNavegable(
                titulo = Screen.FormularioContenido.title,
                rutaActual = Screen.FormularioContenido.route,
                descripcion = "Gestión y publicación de Recetas y Cursos Técnicos.",
                mainViewModel = mainViewModel
            )
        }
    }
}

@Composable
private fun PantallaBaseNavegable(
    titulo: String,
    rutaActual: String,
    descripcion: String,
    mainViewModel: MainViewModel
) {
    Scaffold(
        topBar = {
            MasterMartiniTopBar(
                title = titulo,
                mostrarBotonAtras = true,
                onBackClick = { mainViewModel.navigateBack() }
            )
        },
        bottomBar = {
            MasterMartiniBottomBar(
                currentRoute = rutaActual,
                onNavigate = { destino -> mainViewModel.navigateTo(destino) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { mainViewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}