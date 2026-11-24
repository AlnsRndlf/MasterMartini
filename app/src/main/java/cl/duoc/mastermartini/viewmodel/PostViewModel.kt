package cl.duoc.mastermartini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.data.repository.PostRepository
import cl.duoc.mastermartini.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class PostViewModel(
    private val repository: PostRepository = PostRepository(),
    autoFetch: Boolean = true
) : ViewModel() {

    protected val _postList = MutableStateFlow<List<Post>>(value = emptyList())
    open val postList: StateFlow<List<Post>> = _postList.asStateFlow()

    private val _isLoading = MutableStateFlow(value = false)
    open val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(value = null)
    open val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(value = null)
    open val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    init {
        if (autoFetch) {
            fetchPosts()
        }
    }

    open fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Limitamos visualmente a los primeros 20 posts para una lectura ágil
                _postList.value = repository.getPosts().take(20)
            } catch (e: Exception) {
                _errorMessage.value = "Error al conectar con la API externa: ${e.localizedMessage ?: "Sin conexión"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    open fun crearPost(titulo: String, cuerpo: String) {
        if (titulo.isBlank() || cuerpo.isBlank()) {
            _errorMessage.value = "Debe ingresar título y contenido para publicar"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val nuevo = Post(userId = 1, id = 0, title = titulo.trim(), body = cuerpo.trim())
                val creado = repository.createPost(nuevo)
                val idLocal = if (creado.id <= 0 || _postList.value.any { it.id == creado.id }) {
                    (_postList.value.maxOfOrNull { it.id } ?: 100) + 1
                } else {
                    creado.id
                }
                _postList.value = listOf(creado.copy(id = idLocal)) + _postList.value
                _feedbackMessage.value = "Publicación enviada vía POST (ID #$idLocal)"
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear publicación (POST): ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    open fun actualizarPost(post: Post, nuevoTitulo: String, nuevoCuerpo: String) {
        if (nuevoTitulo.isBlank() || nuevoCuerpo.isBlank()) {
            _errorMessage.value = "El título y el contenido no pueden quedar vacíos"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val actualizado = post.copy(title = nuevoTitulo.trim(), body = nuevoCuerpo.trim())
                // JSONPlaceholder acepta PUT en IDs 1..100; si es creado localmente (>100), enviamos PUT sobre ID 1 como prueba HTTP
                val idEndpoint = if (post.id in 1..100) post.id else 1
                repository.updatePost(idEndpoint, actualizado)
                _postList.value = _postList.value.map { if (it.id == post.id) actualizado else it }
                _feedbackMessage.value = "Publicación #${post.id} actualizada vía PUT"
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar publicación (PUT): ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    open fun eliminarPost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val eliminado = repository.deletePost(postId)
                if (eliminado) {
                    _postList.value = _postList.value.filterNot { it.id == postId }
                    _feedbackMessage.value = "Publicación #$postId eliminada vía DELETE"
                } else {
                    _errorMessage.value = "No se pudo eliminar la publicación #$postId"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar publicación (DELETE): ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarMensajes() {
        _errorMessage.value = null
        _feedbackMessage.value = null
    }
}