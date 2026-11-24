package cl.duoc.mastermartini.data.repository

import cl.duoc.mastermartini.data.remote.BackendRetrofitInstance
import cl.duoc.mastermartini.data.remote.MasterMartiniApiService
import cl.duoc.mastermartini.model.UsuarioBackendDto

open class UsuarioRepository(
    private val backendApi: MasterMartiniApiService = BackendRetrofitInstance.api
) {
    open suspend fun registrarUsuario(usuario: UsuarioBackendDto): UsuarioBackendDto {
        return backendApi.registrarUsuario(usuario)
    }

    open suspend fun obtenerUsuarios(): List<UsuarioBackendDto> {
        return backendApi.obtenerUsuarios()
    }
}