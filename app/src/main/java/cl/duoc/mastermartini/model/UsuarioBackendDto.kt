package cl.duoc.mastermartini.model

data class UsuarioBackendDto(
    val id: Int? = null,
    val tipoPerfil: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val direccion: String,
    val rutEmpresa: String,
    val tipoNegocio: String,
    val tamanoNegocio: String,
    val preferenciasContenido: String,
    val aceptaTerminos: Boolean
)