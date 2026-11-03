package cl.duoc.mastermartini.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mastermartini.ui.screens.ContenidoDetalleScreen
import cl.duoc.mastermartini.ui.screens.ContenidoFormularioScreen
import cl.duoc.mastermartini.ui.screens.ContenidoListaScreen
import cl.duoc.mastermartini.ui.screens.HomeScreen
import cl.duoc.mastermartini.ui.screens.PerfilScreen
import cl.duoc.mastermartini.ui.screens.RegistroScreen
import cl.duoc.mastermartini.ui.screens.ResumenRegistroScreen
import cl.duoc.mastermartini.ui.screens.SettingsScreen
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import cl.duoc.mastermartini.viewmodel.MainViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    contenidoViewModel: ContenidoViewModel = viewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    val destinoRuta = when (val target = event.route) {
                        is Screen.Detail -> target.buildRoute()
                        is Screen.EditarContenido -> target.buildRoute()
                        else -> target.route
                    }
                    navController.navigate(route = destinoRuta) {
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
            ContenidoListaScreen(
                mainViewModel = mainViewModel,
                contenidoViewModel = contenidoViewModel
            )
        }

        composable(route = Screen.FormularioContenido.route) {
            ContenidoFormularioScreen(
                mainViewModel = mainViewModel,
                contenidoViewModel = contenidoViewModel
            )
        }

        composable(
            route = "detail_page/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            ContenidoDetalleScreen(
                contenidoId = id,
                mainViewModel = mainViewModel,
                contenidoViewModel = contenidoViewModel
            )
        }

        composable(
            route = "editar_contenido_page/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ContenidoFormularioScreen(
                mainViewModel = mainViewModel,
                contenidoViewModel = contenidoViewModel
            )
        }
    }
}