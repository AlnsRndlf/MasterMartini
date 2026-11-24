package cl.duoc.mastermartini.data.repository

import cl.duoc.mastermartini.data.remote.ApiService
import cl.duoc.mastermartini.data.remote.RetrofitInstance
import cl.duoc.mastermartini.model.Post

open class PostRepository(
    private val api: ApiService = RetrofitInstance.api
) {

    open suspend fun getPosts(): List<Post> {
        return api.getPosts()
    }

    open suspend fun createPost(post: Post): Post {
        return api.createPost(post)
    }

    open suspend fun updatePost(id: Int, post: Post): Post {
        return api.updatePost(id, post)
    }

    open suspend fun deletePost(id: Int): Boolean {
        val response = api.deletePost(id)
        return response.isSuccessful
    }
}