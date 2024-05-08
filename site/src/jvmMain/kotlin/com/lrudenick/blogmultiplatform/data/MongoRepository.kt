package com.lrudenick.blogmultiplatform.data

import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.model.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User?
    suspend fun checkUserId(id: String): Boolean
    suspend fun addPost(post: Post): Boolean
}