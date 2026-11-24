package cl.duoc.mastermartini.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.data.local.EstadoDataStore
import cl.duoc.mastermartini.data.local.PreferenciasStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstadoViewModel(
    private val storage: PreferenciasStorage,
    private val delayCargaMs: Long = 1500L,
    private val delayMensajeMs: Long = 2000L
) : ViewModel() {

    private val _activo = MutableStateFlow<Boolean?>(value = null)
    val activo: StateFlow<Boolean?> = _activo.asStateFlow()

    private val _mostrarMensaje = MutableStateFlow(value = false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje.asStateFlow()

    private val _altoContraste = MutableStateFlow(value = false)
    val altoContraste: StateFlow<Boolean> = _altoContraste.asStateFlow()

    init {
        cargarEstado()
    }

    fun cargarEstado() {
        viewModelScope.launch {
            _activo.value = null
            if (delayCargaMs > 0) delay(timeMillis = delayCargaMs)
            _activo.value = storage.obtenerEstado().first() ?: false
            _altoContraste.value = storage.obtenerAltoContraste().first()
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            val nuevoValor = !(_activo.value ?: false)
            storage.guardarEstado(nuevoValor)
            _activo.value = nuevoValor
            _mostrarMensaje.value = true
            if (delayMensajeMs > 0) delay(timeMillis = delayMensajeMs)
            _mostrarMensaje.value = false
        }
    }

    fun alternarAltoContraste() {
        viewModelScope.launch {
            val nuevo = !_altoContraste.value
            storage.guardarAltoContraste(nuevo)
            _altoContraste.value = nuevo
            _mostrarMensaje.value = true
            if (delayMensajeMs > 0) delay(timeMillis = delayMensajeMs)
            _mostrarMensaje.value = false
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EstadoViewModel(EstadoDataStore(context.applicationContext)) as T
                }
            }
    }
}