package cl.duoc.mastermartini.model

// almacena errores por campo del formulario
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val rutEmpresa: String? = null,
    val tipoNegocio: String? = null,
    val preferencias: String? = null,
    val terminos: String? = null
)