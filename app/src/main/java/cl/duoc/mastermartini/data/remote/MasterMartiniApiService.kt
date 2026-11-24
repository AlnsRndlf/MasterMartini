package cl.duoc.mastermartini.data.remote

import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.model.UsuarioBackendDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MasterMartiniApiService {

    // --- Endpoints CRUD Contenidos Técnicos ---
    @GET("api/v1/contenidos")
    suspend fun obtenerContenidos(): List<Contenido>

    @POST("api/v1/contenidos")
    suspend fun crearContenido(@Body contenido: Contenido): Contenido

    @PUT("api/v1/contenidos/{id}")
    suspend fun actualizarContenido(
        @Path("id") id: Int,
        @Body contenido: Contenido
    ): Contenido

    @DELETE("api/v1/contenidos/{id}")
    suspend fun eliminarContenido(@Path("id") id: Int): Response<Unit>

    // --- Endpoints Relacionamiento Clientes / Prospectos ---
    @GET("api/v1/usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioBackendDto>

    @POST("api/v1/usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioBackendDto): UsuarioBackendDto
}