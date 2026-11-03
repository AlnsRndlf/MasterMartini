package cl.duoc.mastermartini.data.repository

import cl.duoc.mastermartini.data.local.AppDataBase
import cl.duoc.mastermartini.data.local.ContenidoDao
import cl.duoc.mastermartini.data.local.toDomain
import cl.duoc.mastermartini.data.local.toEntity
import cl.duoc.mastermartini.model.Contenido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContenidoRepository(private val dao: ContenidoDao) {

    val contenidos: Flow<List<Contenido>> = dao.obtenerTodos().map { listaEntities ->
        listaEntities.map { it.toDomain() }
    }

    suspend fun precargarSiEstaVacio() {
        if (dao.contarRegistros() == 0) {
            dao.insertarVarios(AppDataBase.obtenerContenidosIniciales())
        }
    }

    suspend fun obtenerPorId(id: Int): Contenido? {
        return dao.obtenerPorId(id)?.toDomain()
    }

    suspend fun insertar(contenido: Contenido) {
        dao.insertar(contenido.toEntity())
    }

    suspend fun actualizar(contenido: Contenido) {
        dao.actualizar(contenido.toEntity())
    }

    suspend fun eliminar(contenido: Contenido) {
        dao.eliminar(contenido.toEntity())
    }
}