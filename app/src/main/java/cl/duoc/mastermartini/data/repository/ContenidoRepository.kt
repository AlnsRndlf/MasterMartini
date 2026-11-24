package cl.duoc.mastermartini.data.repository

import cl.duoc.mastermartini.data.local.AppDataBase
import cl.duoc.mastermartini.data.local.ContenidoDao
import cl.duoc.mastermartini.data.local.toDomain
import cl.duoc.mastermartini.data.local.toEntity
import cl.duoc.mastermartini.model.Contenido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ContenidoRepository(private val dao: ContenidoDao) {

    open val contenidos: Flow<List<Contenido>> = dao.obtenerTodos().map { listaEntities ->
        listaEntities.map { it.toDomain() }
    }

    open suspend fun precargarSiEstaVacio() {
        if (dao.contarRegistros() == 0) {
            dao.insertarVarios(AppDataBase.obtenerContenidosIniciales())
        }
    }

    open suspend fun obtenerPorId(id: Int): Contenido? {
        return dao.obtenerPorId(id)?.toDomain()
    }

    open suspend fun insertar(contenido: Contenido) {
        dao.insertar(contenido.toEntity())
    }

    open suspend fun actualizar(contenido: Contenido) {
        dao.actualizar(contenido.toEntity())
    }

    open suspend fun eliminar(contenido: Contenido) {
        dao.eliminar(contenido.toEntity())
    }
}