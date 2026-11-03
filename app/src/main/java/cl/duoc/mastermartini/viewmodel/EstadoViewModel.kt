package cl.duoc.mastermartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.data.local.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    // DataStore creado con contexto de aplicación
    private val estadoDataStore = EstadoDataStore(context = application)

    // Estado que representa si el modo especial está activado o no (null = cargando)
    private val _activo = MutableStateFlow<Boolean?>(value = null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    // Estado para mostrar u ocultar el mensaje visual animado
    private val _mostrarMensaje = MutableStateFlow(value = false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()

    // Estado adicional de alto contraste persistente
    private val _altoContraste = MutableStateFlow(value = false)
    val altoContraste: StateFlow<Boolean> = _altoContraste.asStateFlow()

    init {
        // Al iniciar el ViewModel, cargamos el estado desde DataStore
        cargarEstado()
    }

    fun cargarEstado() {
        viewModelScope.launch {
            _activo.value = null
            // Simula demora para mostrar CircularProgressIndicator
            delay(timeMillis = 1500)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
            _altoContraste.value = estadoDataStore.obtenerAltoContraste().first()
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            // Alternamos el valor actual
            val nuevoValor = !(_activo.value ?: false)

            // Guardamos persistentemente en DataStore
            estadoDataStore.guardarEstado(nuevoValor)

            // Actualizamos el flujo reactivo
            _activo.value = nuevoValor

            // Mostramos el mensaje visual animado por 2 segundos
            _mostrarMensaje.value = true
            delay(timeMillis = 2000)
            _mostrarMensaje.value = false
        }
    }

    fun alternarAltoContraste() {
        viewModelScope.launch {
            val nuevo = !_altoContraste.value
            estadoDataStore.guardarAltoContraste(nuevo)
            _altoContraste.value = nuevo
            _mostrarMensaje.value = true
            delay(timeMillis = 2000)
            _mostrarMensaje.value = false
        }
    }
}