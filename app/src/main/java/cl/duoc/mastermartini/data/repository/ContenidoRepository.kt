package cl.duoc.mastermartini.data.repository

import cl.duoc.mastermartini.data.local.AppDataBase
import cl.duoc.mastermartini.data.local.ContenidoDao
import cl.duoc.mastermartini.data.local.toDomain
import cl.duoc.mastermartini.data.local.toEntity
import cl.duoc.mastermartini.data.remote.BackendRetrofitInstance
import cl.duoc.mastermartini.data.remote.MasterMartiniApiService
import cl.duoc.mastermartini.model.Contenido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ContenidoRepository(
    private val dao: ContenidoDao,
    private val backendApi: MasterMartiniApiService = BackendRetrofitInstance.api
) {

    open val contenidos: Flow<List<Contenido>> = dao.obtenerTodos().map { listaEntities ->
        listaEntities.map { it.toDomain() }
    }

    open suspend fun sincronizarConServidor(): Boolean {
        return try {
            val remotos = backendApi.obtenerContenidos()
            dao.eliminarTodos()
            dao.insertarVarios(remotos.map { it.toEntity() })
            true
        } catch (e: Exception) {
            precargarSiEstaVacio()
            false
        }
    }

    open suspend fun precargarSiEstaVacio() {
        if (dao.contarRegistros() == 0) {
            dao.insertarVarios(AppDataBase.obtenerContenidosIniciales())
        }
    }

    open suspend fun obtenerPorId(id: Int): Contenido? {
        return dao.obtenerPorId(id)?.toDomain()
    }

    open suspend fun insertar(contenido: Contenido): Boolean {
        return try {
            val creadoRemoto = backendApi.crearContenido(contenido)
            dao.insertar(creadoRemoto.toEntity())
            true
        } catch (e: Exception) {
            dao.insertar(contenido.toEntity())
            false
        }
    }

    open suspend fun actualizar(contenido: Contenido): Boolean {
        return try {
            val actualizadoRemoto = backendApi.actualizarContenido(contenido.id, contenido)
            dao.actualizar(actualizadoRemoto.toEntity())
            true
        } catch (e: Exception) {
            dao.actualizar(contenido.toEntity())
            false
        }
    }

    open suspend fun eliminar(contenido: Contenido): Boolean {
        return try {
            backendApi.eliminarContenido(contenido.id)
            dao.eliminar(contenido.toEntity())
            true
        } catch (e: Exception) {
            dao.eliminar(contenido.toEntity())
            false
        }
    }
}