package cl.duoc.mastermartini.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.data.local.AppDataBase
import cl.duoc.mastermartini.data.repository.ContenidoRepository
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.model.ContenidoErrores
import cl.duoc.mastermartini.model.ContenidoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContenidoViewModel(
    private val repository: ContenidoRepository,
    sincronizarAlInicio: Boolean = true
) : ViewModel() {

    private val _filtroTipo = MutableStateFlow("Todos")
    val filtroTipo: StateFlow<String> = _filtroTipo.asStateFlow()

    private val _filtroCategoria = MutableStateFlow("Todas")
    val filtroCategoria: StateFlow<String> = _filtroCategoria.asStateFlow()

    private val _formState = MutableStateFlow(ContenidoUiState())
    val formState: StateFlow<ContenidoUiState> = _formState.asStateFlow()

    private val _estadoBackend = MutableStateFlow<String?>(null)
    val estadoBackend: StateFlow<String?> = _estadoBackend.asStateFlow()

    val todosLosContenidos: StateFlow<List<Contenido>> = repository.contenidos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val contenidosFiltrados: StateFlow<List<Contenido>> = combine(
        repository.contenidos,
        _filtroTipo,
        _filtroCategoria
    ) { lista, tipo, categoria ->
        lista
            .filter { tipo == "Todos" || it.tipo.equals(tipo, ignoreCase = true) }
            .filter { categoria == "Todas" || it.categoria.equals(categoria, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        if (sincronizarAlInicio) {
            sincronizarConBackend()
        }
    }

    fun sincronizarConBackend() {
        viewModelScope.launch {
            val conectado = repository.sincronizarConServidor()
            _estadoBackend.value = if (conectado) {
                "Sincronizado con Spring Boot + MySQL"
            } else {
                "Modo local (Room): servidor Spring Boot no disponible"
            }
        }
    }

    fun seleccionarFiltroTipo(tipo: String) {
        _filtroTipo.value = tipo
    }

    fun seleccionarFiltroCategoria(categoria: String) {
        _filtroCategoria.value = categoria
    }

    fun prepararNuevoFormulario() {
        _formState.value = ContenidoUiState()
    }

    fun cargarParaEditar(id: Int) {
        viewModelScope.launch {
            val existente = repository.obtenerPorId(id)
            if (existente != null) {
                _formState.value = ContenidoUiState(
                    idEdicion = existente.id,
                    titulo = existente.titulo,
                    tipo = existente.tipo,
                    categoria = existente.categoria,
                    insumoMartini = existente.insumoMartini,
                    duracion = existente.duracion,
                    pasos = existente.pasos,
                    imagenUri = existente.imagenUri
                )
            }
        }
    }

    fun onTituloChange(valor: String) {
        _formState.update { it.copy(titulo = valor, errores = it.errores.copy(titulo = null)) }
    }

    fun onTipoChange(valor: String) {
        _formState.update { it.copy(tipo = valor) }
    }

    fun onCategoriaChange(valor: String) {
        _formState.update { it.copy(categoria = valor) }
    }

    fun onInsumoChange(valor: String) {
        _formState.update {
            it.copy(insumoMartini = valor, errores = it.errores.copy(insumoMartini = null))
        }
    }

    fun onDuracionChange(valor: String) {
        _formState.update { it.copy(duracion = valor, errores = it.errores.copy(duracion = null)) }
    }

    fun onPasosChange(valor: String) {
        _formState.update { it.copy(pasos = valor, errores = it.errores.copy(pasos = null)) }
    }

    fun onImagenUriChange(uri: String?) {
        _formState.update { it.copy(imagenUri = uri) }
    }

    fun validarYGuardarContenido(): Boolean {
        val actual = _formState.value

        val errores = ContenidoErrores(
            titulo = if (actual.titulo.trim().length < 4) {
                "Ingrese un título descriptivo (mínimo 4 caracteres)"
            } else null,
            insumoMartini = if (actual.insumoMartini.isBlank()) {
                "Indique el insumo Master Martini utilizado"
            } else null,
            duracion = if (actual.duracion.isBlank()) {
                "Indique la duración (ej: 45 min o 3 horas)"
            } else null,
            pasos = if (actual.pasos.trim().length < 10) {
                "Describa los pasos técnicos o temario (mínimo 10 caracteres)"
            } else null
        )

        val hayErrores = listOfNotNull(
            errores.titulo,
            errores.insumoMartini,
            errores.duracion,
            errores.pasos
        ).isNotEmpty()

        _formState.update { it.copy(errores = errores) }

        if (!hayErrores) {
            viewModelScope.launch {
                val contenido = Contenido(
                    id = actual.idEdicion ?: 0,
                    titulo = actual.titulo.trim(),
                    tipo = actual.tipo,
                    categoria = actual.categoria,
                    insumoMartini = actual.insumoMartini.trim(),
                    duracion = actual.duracion.trim(),
                    pasos = actual.pasos.trim(),
                    imagenUri = actual.imagenUri
                )
                val sincronizado = if (actual.idEdicion == null) {
                    repository.insertar(contenido)
                } else {
                    repository.actualizar(contenido)
                }
                _estadoBackend.value = if (sincronizado) {
                    "Guardado en Spring Boot + MySQL"
                } else {
                    "Guardado localmente (sin conexión al servidor)"
                }
                prepararNuevoFormulario()
            }
        }

        return !hayErrores
    }

    fun eliminarContenido(contenido: Contenido) {
        viewModelScope.launch {
            val sincronizado = repository.eliminar(contenido)
            _estadoBackend.value = if (sincronizado) {
                "Eliminado en Spring Boot + MySQL"
            } else {
                "Eliminado localmente (sin conexión al servidor)"
            }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dao = AppDataBase.getDatabase(context.applicationContext).contenidoDao()
                    return ContenidoViewModel(ContenidoRepository(dao)) as T
                }
            }
    }
}