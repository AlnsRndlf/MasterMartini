package cl.duoc.mastermartini

import cl.duoc.mastermartini.data.local.ContenidoDao
import cl.duoc.mastermartini.data.local.ContenidoEntity
import cl.duoc.mastermartini.data.remote.MasterMartiniApiService
import cl.duoc.mastermartini.data.repository.ContenidoRepository
import cl.duoc.mastermartini.model.Contenido
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import retrofit2.Response

class ContenidoRepositoryTest : StringSpec({

    val ejemplo = Contenido(
        id = 1,
        titulo = "Bombones Ariba",
        tipo = "Receta",
        categoria = "Chocolatería",
        insumoMartini = "Chocolate Ariba",
        duracion = "45 min",
        pasos = "Templar y moldear chocolate"
    )

    val entidadEjemplo = ContenidoEntity(
        id = 1,
        titulo = "Bombones Ariba",
        tipo = "Receta",
        categoria = "Chocolatería",
        insumoMartini = "Chocolate Ariba",
        duracion = "45 min",
        pasos = "Templar y moldear chocolate"
    )

    "contenidos Flow debe mapear entidades de Room a modelos de dominio" {
        val mockDao = mockk<ContenidoDao>()
        val mockApi = mockk<MasterMartiniApiService>()
        every { mockDao.obtenerTodos() } returns flowOf(listOf(entidadEjemplo))

        val repo = ContenidoRepository(mockDao, mockApi)

        runTest {
            repo.contenidos.first() shouldBe listOf(ejemplo)
        }
    }

    "sincronizarConServidor() debe reemplazar Room con datos remotos cuando Spring Boot responde" {
        val mockDao = mockk<ContenidoDao>()
        val mockApi = mockk<MasterMartiniApiService>()
        every { mockDao.obtenerTodos() } returns flowOf(emptyList())
        coEvery { mockApi.obtenerContenidos() } returns listOf(ejemplo)
        coEvery { mockDao.eliminarTodos() } just Runs
        coEvery { mockDao.insertarVarios(any()) } just Runs

        val repo = ContenidoRepository(mockDao, mockApi)

        runTest {
            repo.sincronizarConServidor().shouldBeTrue()
            coVerify(exactly = 1) { mockDao.eliminarTodos() }
            coVerify(exactly = 1) { mockDao.insertarVarios(any()) }
        }
    }

    "sincronizarConServidor() debe hacer fallback a precarga local si el backend falla" {
        val mockDao = mockk<ContenidoDao>()
        val mockApi = mockk<MasterMartiniApiService>()
        every { mockDao.obtenerTodos() } returns flowOf(emptyList())
        coEvery { mockApi.obtenerContenidos() } throws RuntimeException("Sin conexión")
        coEvery { mockDao.contarRegistros() } returns 0
        coEvery { mockDao.insertarVarios(any()) } just Runs

        val repo = ContenidoRepository(mockDao, mockApi)

        runTest {
            repo.sincronizarConServidor().shouldBeFalse()
            coVerify(exactly = 1) { mockDao.insertarVarios(any()) }
        }
    }

    "insertar(), actualizar() y eliminar() deben sincronizar con backend y replicar en Room" {
        val mockDao = mockk<ContenidoDao>()
        val mockApi = mockk<MasterMartiniApiService>()
        every { mockDao.obtenerTodos() } returns flowOf(emptyList())
        coEvery { mockApi.crearContenido(ejemplo) } returns ejemplo
        coEvery { mockApi.actualizarContenido(1, ejemplo) } returns ejemplo
        coEvery { mockApi.eliminarContenido(1) } returns Response.success(Unit)
        coEvery { mockDao.insertar(any()) } just Runs
        coEvery { mockDao.actualizar(any()) } just Runs
        coEvery { mockDao.eliminar(any()) } just Runs
        coEvery { mockDao.obtenerPorId(1) } returns entidadEjemplo

        val repo = ContenidoRepository(mockDao, mockApi)

        runTest {
            repo.insertar(ejemplo).shouldBeTrue()
            repo.actualizar(ejemplo).shouldBeTrue()
            repo.eliminar(ejemplo).shouldBeTrue()
            repo.obtenerPorId(1) shouldBe ejemplo
        }
    }

    "insertar(), actualizar() y eliminar() deben operar localmente en Room si el servidor falla" {
        val mockDao = mockk<ContenidoDao>()
        val mockApi = mockk<MasterMartiniApiService>()
        every { mockDao.obtenerTodos() } returns flowOf(emptyList())
        coEvery { mockApi.crearContenido(any()) } throws RuntimeException("Offline")
        coEvery { mockApi.actualizarContenido(any(), any()) } throws RuntimeException("Offline")
        coEvery { mockApi.eliminarContenido(any()) } throws RuntimeException("Offline")
        coEvery { mockDao.insertar(any()) } just Runs
        coEvery { mockDao.actualizar(any()) } just Runs
        coEvery { mockDao.eliminar(any()) } just Runs

        val repo = ContenidoRepository(mockDao, mockApi)

        runTest {
            repo.insertar(ejemplo).shouldBeFalse()
            repo.actualizar(ejemplo).shouldBeFalse()
            repo.eliminar(ejemplo).shouldBeFalse()
            coVerify(exactly = 1) { mockDao.insertar(any()) }
            coVerify(exactly = 1) { mockDao.actualizar(any()) }
            coVerify(exactly = 1) { mockDao.eliminar(any()) }
        }
    }
})