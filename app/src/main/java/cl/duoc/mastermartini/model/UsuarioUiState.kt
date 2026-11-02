package cl.duoc.mastermartini.model

// estado reactivo del formulario
data class UsuarioUiState(
    val tipoPerfil: String = "Cliente", // cliente o prospecto
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val rutEmpresa: String = "",
    val tipoNegocio: String = "Pastelería",
    val tamanoNegocio: String = "Pyme (2 a 10 personas)",
    val preferenciasContenido: List<String> = emptyList(),
    val aceptaTerminos: Boolean = false,
    val registroCompletado: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)