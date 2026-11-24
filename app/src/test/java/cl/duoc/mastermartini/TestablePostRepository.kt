package cl.duoc.mastermartini

import cl.duoc.mastermartini.data.remote.ApiService
import cl.duoc.mastermartini.data.repository.PostRepository
import cl.duoc.mastermartini.model.Post
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response

// Guía 15 Parte 4: Subclase de PostRepository para inyectar el ApiService simulado
class TestablePostRepository(private val testApi: ApiService) : PostRepository(testApi) {
    override suspend fun getPosts(): List<Post> = testApi.getPosts()
}

class PostRepositoryTest : StringSpec({

    "getPosts() debe retornar la lista de posts simulada desde ApiService" {
        val fakePosts = listOf(
            Post(userId = 1, id = 1, title = "Título 1", body = "Cuerpo 1"),
            Post(userId = 2, id = 2, title = "Título 2", body = "Cuerpo 2")
        )
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts

        val repo = TestablePostRepository(testApi = mockApi)

        runTest {
            val result = repo.getPosts()
            result shouldContainExactly fakePosts
            coVerify(exactly = 1) { mockApi.getPosts() }
        }
    }

    "createPost(), updatePost() y deletePost() deben delegar correctamente a ApiService" {
        val mockApi = mockk<ApiService>()
        val postNuevo = Post(userId = 1, id = 0, title = "Receta Brioche", body = "Contenido")
        val postCreado = postNuevo.copy(id = 101)
        val postEditado = postCreado.copy(title = "Receta Brioche Editada")

        coEvery { mockApi.createPost(postNuevo) } returns postCreado
        coEvery { mockApi.updatePost(1, postEditado) } returns postEditado
        coEvery { mockApi.deletePost(1) } returns Response.success(Unit)

        val repo = PostRepository(api = mockApi)

        runTest {
            repo.createPost(postNuevo) shouldBe postCreado
            repo.updatePost(1, postEditado) shouldBe postEditado
            repo.deletePost(1).shouldBeTrue()
        }
    }
})