package cl.duoc.mastermartini.navigation

sealed class Screen(val route: String, val title: String) {

    // rutas principales sin argumentos
    data object Home : Screen(route = "home_page", title = "Inicio")
    data object Catalogo : Screen(route = "catalogo_page", title = "Catálogo Técnico")
    data object FormularioContenido : Screen(route = "formulario_contenido_page", title = "Subir Receta")
    data object Registro : Screen(route = "registro_page", title = "Registro Cliente")
    data object ResumenRegistro : Screen(route = "resumen_registro_page", title = "Resumen Registro")
    data object Profile : Screen(route = "profile_page", title = "Mi Perfil")
    data object Settings : Screen(route = "settings_page", title = "Accesibilidad")

    // ruta con argumentos para ver el detalle de una receta, curso o video
    data class Detail(val itemId: String) : Screen(
        route = "detail_page/{itemId}",
        title = "Detalle del Contenido"
    ) {
        fun buildRoute(): String {
            return route.replace(oldValue = "{itemId}", newValue = itemId)
        }
    }

    // ruta con argumentos para editar un contenido existente en el CRUD
    data class EditarContenido(val itemId: String) : Screen(
        route = "editar_contenido_page/{itemId}",
        title = "Editar Contenido"
    ) {
        fun buildRoute(): String {
            return route.replace(oldValue = "{itemId}", newValue = itemId)
        }
    }
}