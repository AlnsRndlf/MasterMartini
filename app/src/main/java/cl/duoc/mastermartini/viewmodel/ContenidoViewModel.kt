package cl.duoc.mastermartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class ContenidoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContenidoRepository

    // Filtros reactivos para el catálogo (Receta, Curso, Video Técnico y Categoría)
    private val _filtroTipo = MutableStateFlow("Todos")
    val filtroTipo: StateFlow<String> = _filtroTipo.asStateFlow()

    private val _filtroCategoria = MutableStateFlow("Todas")
    val filtroCategoria: StateFlow<String> = _filtroCategoria.asStateFlow()

    // Estado del formulario de creación/edición
    private val _formState = MutableStateFlow(ContenidoUiState())
    val formState: StateFlow<ContenidoUiState> = _formState.asStateFlow()

    // Lista de contenidos filtrada reactivamente usando .filter (Guía 3)
    val contenidosFiltrados: StateFlow<List<Contenido>>

    init {
        val dao = AppDataBase.getDatabase(application).contenidoDao()
        repository = ContenidoRepository(dao)

        viewModelScope.launch {
            repository.precargarSiEstaVacio()
        }

        contenidosFiltrados = combine(
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
    }

    fun seleccionarFiltroTipo(tipo: String) {
        _filtroTipo.value = tipo
    }

    fun seleccionarFiltroCategoria(categoria: String) {
        _filtroCategoria.value = categoria
    }

    // --- Gestión del Formulario CRUD ---
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

    // Validación desacoplada del formulario de Contenidos Técnicos (IE 2.2.1)
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
                if (actual.idEdicion == null) {
                    repository.insertar(contenido)
                } else {
                    repository.actualizar(contenido)
                }
                prepararNuevoFormulario()
            }
        }

        return !hayErrores
    }

    fun eliminarContenido(contenido: Contenido) {
        viewModelScope.launch {
            repository.eliminar(contenido)
        }
    }
}