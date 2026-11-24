package cl.duoc.mastermartini

import android.net.Uri
import cl.duoc.mastermartini.data.remote.MasterMartiniApiService
import cl.duoc.mastermartini.data.repository.UsuarioRepository
import cl.duoc.mastermartini.model.UsuarioBackendDto
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
    }

    afterEach {
        Dispatchers.resetMain()
    }

    "validarFormulario() debe retornar false y poblar errores cuando los datos son inválidos" {
        val mockRepo = mockk<UsuarioRepository>()
        val vm = UsuarioViewModel(mockRepo)

        vm.validarFormulario().shouldBeFalse()
        val errores = vm.estado.value.errores
        errores.nombre.shouldNotBeNull()
        errores.correo.shouldNotBeNull()
        errores.clave.shouldNotBeNull()
        errores.telefono.shouldNotBeNull()
        errores.direccion.shouldNotBeNull()
        errores.rutEmpresa.shouldNotBeNull()
        errores.preferencias.shouldNotBeNull()
        errores.terminos.shouldNotBeNull()
    }

    "validarFormulario() válido debe enviar el usuario al microservicio y guardar confirmación" {
        val mockRepo = mockk<UsuarioRepository>()
        val dtoRespuesta = UsuarioBackendDto(
            id = 10,
            tipoPerfil = "Prospecto",
            nombre = "María Pastelería",
            correo = "maria@pasteleria.cl",
            telefono = "+56987654321",
            direccion = "Av. Grecia 1234",
            rutEmpresa = "76.123.456-0",
            tipoNegocio = "Panadería",
            tamanoNegocio = "Microemprendimiento",
            preferenciasContenido = "Chocolatería",
            aceptaTerminos = true
        )
        coEvery { mockRepo.registrarUsuario(any()) } returns dtoRespuesta

        runTest(testDispatcher) {
            val vm = UsuarioViewModel(mockRepo)
            vm.onTipoPerfilChange("Prospecto")
            vm.onNombreChange("María Pastelería")
            vm.onCorreoChange("maria@pasteleria.cl")
            vm.onClaveChange("123456")
            vm.onTelefonoChange("+56987654321")
            vm.onDireccionChange("Av. Grecia 1234")
            vm.onRutEmpresaChange("76.123.456-0")
            vm.onTipoNegocioChange("Panadería")
            vm.onTamanoNegocioChange("Microemprendimiento")
            vm.onTogglePreferencia("Chocolatería")
            vm.onAceptarTerminosChange(true)

            vm.validarFormulario().shouldBeTrue()
            advanceUntilIdle()

            vm.estado.value.registroCompletado.shouldBeTrue()
            vm.estado.value.mensajeBackend!! shouldContain "ID #10"
            coVerify(exactly = 1) { mockRepo.registrarUsuario(any()) }
        }
    }

    "onTogglePreferencia y actualización de imagen cámara/galería deben actualizar los StateFlow" {
        val mockRepo = mockk<UsuarioRepository>()
        val vm = UsuarioViewModel(mockRepo)
        val mockUriGaleria = mockk<Uri>()
        val mockUriCamara = mockk<Uri>()

        vm.onTogglePreferencia("Heladería")
        vm.estado.value.preferenciasContenido shouldContain "Heladería"
        vm.onTogglePreferencia("Heladería")
        vm.estado.value.preferenciasContenido.shouldBeEmpty()

        vm.actualizarImagenGaleria(mockUriGaleria)
        vm.imagenPerfilUri.value shouldBe mockUriGaleria

        vm.actualizarImagenCamara(mockUriCamara)
        vm.imagenPerfilUri.value shouldBe mockUriCamara
    }

    "UsuarioRepository debe delegar registrarUsuario y obtenerUsuarios al backend" {
        val mockApi = mockk<MasterMartiniApiService>()
        val dto = UsuarioBackendDto(
            id = 1,
            tipoPerfil = "Cliente",
            nombre = "Alonso",
            correo = "a@b.cl",
            telefono = "912345678",
            direccion = "Santiago",
            rutEmpresa = "11.111.111-1",
            tipoNegocio = "Pastelería",
            tamanoNegocio = "Pyme",
            preferenciasContenido = "Bollería",
            aceptaTerminos = true
        )
        coEvery { mockApi.registrarUsuario(dto) } returns dto
        coEvery { mockApi.obtenerUsuarios() } returns listOf(dto)

        val repo = UsuarioRepository(mockApi)

        runTest {
            repo.registrarUsuario(dto) shouldBe dto
            repo.obtenerUsuarios() shouldBe listOf(dto)
        }
    }
})