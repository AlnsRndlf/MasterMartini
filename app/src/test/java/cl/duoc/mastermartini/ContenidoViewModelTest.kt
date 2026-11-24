package cl.duoc.mastermartini

import cl.duoc.mastermartini.data.repository.ContenidoRepository
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ContenidoViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
    }

    afterEach {
        Dispatchers.resetMain()
    }

    "validarYGuardarContenido() debe detectar errores cuando los campos están vacíos" {
        val mockRepo = mockk<ContenidoRepository>()
        every { mockRepo.contenidos } returns MutableStateFlow(emptyList())

        runTest(testDispatcher) {
            val vm = ContenidoViewModel(mockRepo, sincronizarAlInicio = false)
            vm.validarYGuardarContenido().shouldBeFalse()

            val errores = vm.formState.value.errores
            errores.titulo.shouldNotBeNull()
            errores.insumoMartini.shouldNotBeNull()
            errores.duracion.shouldNotBeNull()
            errores.pasos.shouldNotBeNull()
        }
    }

    "validarYGuardarContenido() debe llamar a insertar() cuando idEdicion es nulo y a actualizar() cuando existe" {
        val mockRepo = mockk<ContenidoRepository>()
        val existente = Contenido(
            id = 5,
            titulo = "Croissant Hoja",
            tipo = "Curso",
            categoria = "Masas de hoja",
            insumoMartini = "Margarina Gioia",
            duracion = "3 horas",
            pasos = "Empaste y tres vueltas simples en frío"
        )
        every { mockRepo.contenidos } returns MutableStateFlow(listOf(existente))
        coEvery { mockRepo.insertar(any()) } returns true
        coEvery { mockRepo.actualizar(any()) } returns true
        coEvery { mockRepo.obtenerPorId(5) } returns existente

        runTest(testDispatcher) {
            val vm = ContenidoViewModel(mockRepo, sincronizarAlInicio = false)

            // Caso 1: Inserción nueva
            vm.onTituloChange("Tarta de Chocolate")
            vm.onTipoChange("Receta")
            vm.onCategoriaChange("Chocolatería")
            vm.onInsumoChange("Chocolate Ariba 54%")
            vm.onDuracionChange("50 min")
            vm.onPasosChange("1. Preparar masa sablée y rellenar con ganache.")
            vm.onImagenUriChange("content://foto.jpg")

            vm.validarYGuardarContenido().shouldBeTrue()
            advanceUntilIdle()
            coVerify(exactly = 1) { mockRepo.insertar(any()) }
            vm.estadoBackend.value!! shouldContain "Guardado en Spring Boot"

            // Caso 2: Edición existente
            vm.cargarParaEditar(5)
            advanceUntilIdle()
            vm.formState.value.idEdicion shouldBe 5

            vm.validarYGuardarContenido().shouldBeTrue()
            advanceUntilIdle()
            coVerify(exactly = 1) { mockRepo.actualizar(any()) }
        }
    }

    "contenidosFiltrados debe filtrar por tipo y categoría reactivamente" {
        val receta = Contenido(1, "Bombones", "Receta", "Chocolatería", "Ariba", "30m", "Pasos largos")
        val curso = Contenido(2, "Croissant", "Curso", "Masas de hoja", "Gioia", "4h", "Pasos largos")
        val mockRepo = mockk<ContenidoRepository>()
        every { mockRepo.contenidos } returns MutableStateFlow(listOf(receta, curso))

        runTest(testDispatcher) {
            val vm = ContenidoViewModel(mockRepo, sincronizarAlInicio = false)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                vm.contenidosFiltrados.collect()
            }
            advanceUntilIdle()
            vm.contenidosFiltrados.value shouldHaveSize 2

            vm.seleccionarFiltroTipo("Curso")
            advanceUntilIdle()
            vm.contenidosFiltrados.value shouldHaveSize 1
            vm.contenidosFiltrados.value.first().titulo shouldBe "Croissant"

            vm.seleccionarFiltroTipo("Todos")
            vm.seleccionarFiltroCategoria("Chocolatería")
            advanceUntilIdle()
            vm.contenidosFiltrados.value shouldHaveSize 1
            vm.contenidosFiltrados.value.first().titulo shouldBe "Bombones"
        }
    }

    "eliminarContenido() y sincronizarConBackend() deben actualizar estadoBackend" {
        val item = Contenido(1, "Bombones", "Receta", "Chocolatería", "Ariba", "30m", "Pasos largos")
        val mockRepo = mockk<ContenidoRepository>()
        every { mockRepo.contenidos } returns MutableStateFlow(listOf(item))
        coEvery { mockRepo.sincronizarConServidor() } returns true
        coEvery { mockRepo.eliminar(item) } returns true

        runTest(testDispatcher) {
            val vm = ContenidoViewModel(mockRepo, sincronizarAlInicio = true)
            advanceUntilIdle()
            vm.estadoBackend.value!! shouldContain "Sincronizado"

            vm.eliminarContenido(item)
            advanceUntilIdle()
            coVerify(exactly = 1) { mockRepo.eliminar(item) }
            vm.estadoBackend.value!! shouldContain "Eliminado"
        }
    }
})