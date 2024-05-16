package com.lrudenick.blogmultiplatform.androidapp.data

import com.lrudenick.blogmultiplatform.androidapp.model.Post
import com.lrudenick.blogmultiplatform.androidapp.util.RequestState
import com.lrudenick.blogmultiplatform.model.Category
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<Post>>>
    fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>>
    fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>>
}