package cl.duoc.mastermartini.navigation

sealed class NavigationEvent {

    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = true
    ) : NavigationEvent()

    // evento para volver a la pantalla anterior en la pila de navegación
    data object PopBackStack : NavigationEvent()

    // evento para navegar hacia arriba en la jerarquía de la aplicación
    data object NavigateUp : NavigationEvent()
}