package cl.duoc.mastermartini

import cl.duoc.mastermartini.navigation.NavigationEvent
import cl.duoc.mastermartini.navigation.Screen
import cl.duoc.mastermartini.viewmodel.MainViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
    }

    afterEach {
        Dispatchers.resetMain()
    }

    "MainViewModel debe emitir eventos NavigateTo, PopBackStack y NavigateUp correctamente" {
        runTest(testDispatcher) {
            val vm = MainViewModel()
            val eventosRecibidos = mutableListOf<NavigationEvent>()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                vm.navigationEvents.collect { eventosRecibidos.add(it) }
            }

            vm.navigateTo(Screen.Catalogo)
            vm.navigateBack()
            vm.navigateUp()
            advanceUntilIdle()

            eventosRecibidos shouldHaveSize 3
            (eventosRecibidos[0] as NavigationEvent.NavigateTo).route shouldBe Screen.Catalogo
            eventosRecibidos[1] shouldBe NavigationEvent.PopBackStack
            eventosRecibidos[2] shouldBe NavigationEvent.NavigateUp
        }
    }

    "Screen.Detail y Screen.EditarContenido deben construir rutas con argumentos correctamente" {
        Screen.Detail("42").buildRoute() shouldBe "detail_page/42"
        Screen.EditarContenido("7").buildRoute() shouldBe "editar_contenido_page/7"
    }
})