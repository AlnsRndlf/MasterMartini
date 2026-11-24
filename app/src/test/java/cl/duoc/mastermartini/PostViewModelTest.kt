package cl.duoc.mastermartini

import cl.duoc.mastermartini.data.repository.PostRepository
import cl.duoc.mastermartini.model.Post
import cl.duoc.mastermartini.viewmodel.PostViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({

    val testDispatcher = StandardTestDispatcher()

    beforeEach {
        Dispatchers.setMain(testDispatcher)
    }

    afterEach {
        Dispatchers.resetMain()
    }

    "postList debe contener los datos esperados después de fetchPosts()" {
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Contenido 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Contenido 2")
        )
        val mockRepo = mockk<PostRepository>()
        coEvery { mockRepo.getPosts() } returns fakePosts

        runTest(testDispatcher) {
            val viewModel = PostViewModel(repository = mockRepo, autoFetch = false)
            viewModel.fetchPosts()
            advanceUntilIdle()

            viewModel.postList.value shouldContainExactly fakePosts
            viewModel.isLoading.value shouldBe false
            viewModel.errorMessage.value.shouldBeNull()
        }
    }

    "fetchPosts() debe informar error cuando la API falla" {
        val mockRepo = mockk<PostRepository>()
        coEvery { mockRepo.getPosts() } throws RuntimeException("Timeout")

        runTest(testDispatcher) {
            val viewModel = PostViewModel(repository = mockRepo, autoFetch = false)
            viewModel.fetchPosts()
            advanceUntilIdle()

            viewModel.errorMessage.value!! shouldContain "Timeout"
            viewModel.isLoading.value shouldBe false
        }
    }

    "crearPost(), actualizarPost() y eliminarPost() deben sincronizar la lista y mensajes" {
        val mockRepo = mockk<PostRepository>()
        val postInicial = Post(userId = 1, id = 1, title = "Original", body = "Cuerpo")
        val postCreado = Post(userId = 1, id = 101, title = "Nuevo", body = "Detalle")

        coEvery { mockRepo.getPosts() } returns listOf(postInicial)
        coEvery { mockRepo.createPost(any()) } returns postCreado
        coEvery { mockRepo.updatePost(any(), any()) } returns postInicial.copy(title = "Editado")
        coEvery { mockRepo.deletePost(1) } returns true

        runTest(testDispatcher) {
            val viewModel = PostViewModel(repository = mockRepo, autoFetch = true)
            advanceUntilIdle()

            // Validación de campos vacíos
            viewModel.crearPost("", "")
            viewModel.errorMessage.value!! shouldContain "Debe ingresar título"

            // POST
            viewModel.crearPost("Nuevo", "Detalle")
            advanceUntilIdle()
            viewModel.postList.value shouldHaveSize 2
            viewModel.feedbackMessage.value!! shouldContain "POST"

            // PUT
            viewModel.actualizarPost(postInicial, "Editado", "Cuerpo editado")
            advanceUntilIdle()
            viewModel.postList.value.first { it.id == 1 }.title shouldBe "Editado"
            viewModel.feedbackMessage.value!! shouldContain "PUT"

            // DELETE
            viewModel.eliminarPost(1)
            advanceUntilIdle()
            viewModel.postList.value shouldHaveSize 1
            viewModel.feedbackMessage.value!! shouldContain "DELETE"

            viewModel.limpiarMensajes()
            viewModel.feedbackMessage.value.shouldBeNull()
            viewModel.errorMessage.value.shouldBeNull()
        }
    }
})