package com.lrudenick.blogmultiplatform.data

import com.lrudenick.blogmultiplatform.model.Category
import com.lrudenick.blogmultiplatform.model.Newsletter
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.model.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User?
    suspend fun checkUserId(id: String): Boolean
    suspend fun addPost(post: Post): Boolean
    suspend fun fetchMyPosts(skip: Int, author: String): List<Post>
    suspend fun fetchMainPosts(): List<Post>
    suspend fun fetchLatestPosts(skip: Int): List<Post>
    suspend fun fetchSponsoredPosts(): List<Post>
    suspend fun fetchPopularPosts(skip: Int): List<Post>
    suspend fun deleteSelectedPosts(ids: List<String>): Boolean
    suspend fun searchPostsByTitle(query: String, skip: Int): List<Post>
    suspend fun searchPostsByCategory(category: Category, skip: Int): List<Post>
    suspend fun fetchSelectedPost(postId: String): Post?
    suspend fun updatePost(post: Post): Boolean
    suspend fun subscribe(newsletter: Newsletter): String
}