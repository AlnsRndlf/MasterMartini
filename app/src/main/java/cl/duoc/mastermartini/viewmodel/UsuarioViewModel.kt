package cl.duoc.mastermartini.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mastermartini.model.UsuarioErrores
import cl.duoc.mastermartini.model.UsuarioUiState
import cl.duoc.mastermartini.ui.utils.ValidadorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


// gestion del estado y validacion del formulario desaclopado
class UsuarioViewModel : ViewModel() {

    // Estado interno mutable
    private val _estado = MutableStateFlow(value = UsuarioUiState())

    // Estado expuesto de solo lectura para la UI
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    fun onTipoPerfilChange(valor: String) {
        _estado.update { it.copy(tipoPerfil = valor) }
    }

    // Actualiza el campo nombre y limpia su error
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(nombre = valor, errores = it.errores.copy(nombre = null))
        }
    }

    // Actualiza el campo correo y limpia su error
    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(correo = valor, errores = it.errores.copy(correo = null))
        }
    }

    // Actualiza el campo clave y limpia su error
    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(clave = valor, errores = it.errores.copy(clave = null))
        }
    }

    // Actualiza el campo telefono y limpia su error
    fun onTelefonoChange(valor: String) {
        _estado.update {
            it.copy(telefono = valor, errores = it.errores.copy(telefono = null))
        }
    }

    // Actualiza el campo direccion y limpia su error
    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(direccion = valor, errores = it.errores.copy(direccion = null))
        }
    }

    // Actualiza el rut de empresa y limpia su error
    fun onRutEmpresaChange(valor: String) {
        _estado.update {
            it.copy(rutEmpresa = valor, errores = it.errores.copy(rutEmpresa = null))
        }
    }

    fun onTipoNegocioChange(valor: String) {
        _estado.update {
            it.copy(tipoNegocio = valor, errores = it.errores.copy(tipoNegocio = null))
        }
    }

    fun onTamanoNegocioChange(valor: String) {
        _estado.update { it.copy(tamanoNegocio = valor) }
    }

    // Agrega o quita una especialidad de interes en la encuesta de segmentacion
    fun onTogglePreferencia(especialidad: String) {
        _estado.update { estadoActual ->
            val nuevasPreferencias = if (estadoActual.preferenciasContenido.contains(especialidad)) {
                estadoActual.preferenciasContenido - especialidad
            } else {
                estadoActual.preferenciasContenido + especialidad
            }
            estadoActual.copy(
                preferenciasContenido = nuevasPreferencias,
                errores = estadoActual.errores.copy(preferencias = null)
            )
        }
    }

    // Actualiza checkbox de aceptacion de terminos
    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(aceptaTerminos = valor, errores = it.errores.copy(terminos = null))
        }
    }


    // Validacion global y centralizada del formulario
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) {
                "El nombre completo es obligatorio"
            } else null,
            correo = if (!ValidadorUtils.esCorreoValido(estadoActual.correo)) {
                "Correo inválido (debe incluir @ y dominio)"
            } else null,
            clave = if (estadoActual.clave.length < 6) {
                "La contraseña debe tener al menos 6 caracteres"
            } else null,
            telefono = if (!ValidadorUtils.esTelefonoValido(estadoActual.telefono)) {
                "Ingrese un teléfono válido (8 a 11 dígitos)"
            } else null,
            direccion = if (estadoActual.direccion.isBlank()) {
                "La dirección comercial es obligatoria"
            } else null,
            rutEmpresa = if (!ValidadorUtils.esRutValido(estadoActual.rutEmpresa)) {
                "RUT inválido (ejemplo válido: 76.123.456-0)"
            } else null,
            tipoNegocio = if (estadoActual.tipoNegocio.isBlank()) {
                "Seleccione un rubro gastronómico"
            } else null,
            preferencias = if (estadoActual.preferenciasContenido.isEmpty()) {
                "Seleccione al menos 1 área de interés técnico"
            } else null,
            terminos = if (!estadoActual.aceptaTerminos) {
                "Debe aceptar los términos para continuar"
            } else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.telefono,
            errores.direccion,
            errores.rutEmpresa,
            errores.tipoNegocio,
            errores.preferencias,
            errores.terminos
        ).isNotEmpty()

        _estado.update {
            it.copy(
                errores = errores,
                registroCompletado = !hayErrores
            )
        }

        return !hayErrores
    }
}