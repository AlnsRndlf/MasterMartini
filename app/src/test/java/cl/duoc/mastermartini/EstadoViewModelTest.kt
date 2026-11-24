package cl.duoc.mastermartini

import cl.duoc.mastermartini.data.local.PreferenciasStorage
import cl.duoc.mastermartini.viewmodel.EstadoViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class EstadoViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
    }

    afterEach {
        Dispatchers.resetMain()
    }

    "cargarEstado(), alternarEstado() y alternarAltoContraste() deben persistir y actualizar flujos" {
        val mockStorage = mockk<PreferenciasStorage>()
        every { mockStorage.obtenerEstado() } returns flowOf(false)
        every { mockStorage.obtenerAltoContraste() } returns flowOf(false)
        coEvery { mockStorage.guardarEstado(any()) } just Runs
        coEvery { mockStorage.guardarAltoContraste(any()) } just Runs

        runTest(testDispatcher) {
            val vm = EstadoViewModel(
                storage = mockStorage,
                delayCargaMs = 0L,
                delayMensajeMs = 0L
            )
            advanceUntilIdle()

            vm.activo.value shouldBe false
            vm.altoContraste.value shouldBe false

            vm.alternarEstado()
            advanceUntilIdle()
            vm.activo.value shouldBe true
            coVerify(exactly = 1) { mockStorage.guardarEstado(true) }

            vm.alternarAltoContraste()
            advanceUntilIdle()
            vm.altoContraste.value shouldBe true
            coVerify(exactly = 1) { mockStorage.guardarAltoContraste(true) }
        }
    }
})