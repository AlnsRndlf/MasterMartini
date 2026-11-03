package cl.duoc.mastermartini.model

data class Contenido(
    val id: Int = 0,
    val titulo: String,
    val tipo: String,          // receta, curso, video tecnico
    val categoria: String,     // ej pasteleria, postre, chocolateria, etc
    val insumoMartini: String, // Insumo principal de Master Martini utilizado
    val duracion: String,      // 45min - 3h, 2 dias, tiempo
    val pasos: String,         // instrucciones o temario
    val imagenUri: String? = null
)

// errores para el formulario
data class ContenidoErrores(
    val titulo: String? = null,
    val insumoMartini: String? = null,
    val duracion: String? = null,
    val pasos: String? = null
)

// estado del formulario de contenidos tecnicos
data class ContenidoUiState(
    val idEdicion: Int? = null,
    val titulo: String = "",
    val tipo: String = "Receta",
    val categoria: String = "Chocolatería",
    val insumoMartini: String = "",
    val duracion: String = "",
    val pasos: String = "",
    val imagenUri: String? = null,
    val errores: ContenidoErrores = ContenidoErrores()
)