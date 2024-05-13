package com.lrudenick.blogmultiplatform.api

import com.lrudenick.blogmultiplatform.data.MongoDB
import com.lrudenick.blogmultiplatform.model.ApiListResponse
import com.lrudenick.blogmultiplatform.model.Constants.AUTHOR_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.POST_ID_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.QUERY_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.SKIP_PARAM
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.util.getBody
import com.lrudenick.blogmultiplatform.util.setBody
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = "addpost")
suspend fun addPost(context: ApiContext) {
    try {
        val post = context.req.getBody<Post>()
        val newPost = post?.copy(id = ObjectIdGenerator.generateNewId<String>().toString())
        val result = newPost?.let {
            context.data.getValue<MongoDB>().addPost(newPost)
        } ?: false
        context.res.setBodyText(result.toString())
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}

@Api(routeOverride = "fetchmyposts")
suspend fun fetchMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val author = context.req.params[AUTHOR_PARAM] ?: ""
        val posts = context.data.getValue<MongoDB>().getMyPosts(skip, author)
        context.res.setBody(ApiListResponse.Success(data = posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message.toString()))
    }
}

@Api(routeOverride = "deleteselectedposts")
suspend fun deleteSelectedPosts(context: ApiContext) {
    try {
        val ids = context.req.getBody<List<String>>()
        val result = ids?.let {
            context.data.getValue<MongoDB>().deleteSelectedPosts(ids)
        } ?: false
        context.res.setBodyText(result.toString())
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}

@Api(routeOverride = "searchposts")
suspend fun searchPostsByTitle(context: ApiContext) {
    try {
        val query = context.req.params[QUERY_PARAM] ?: ""
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val posts = context.data.getValue<MongoDB>().searchPostsByTitle(query, skip)
        context.res.setBody(ApiListResponse.Success(data = posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message.toString()))
    }
}

@Api(routeOverride = "fetchselectedpost")
suspend fun fetchSelectedPost(context: ApiContext) {
    try {
        val postId = context.req.params[POST_ID_PARAM] ?: ""
        val post = context.data.getValue<MongoDB>().fetchSelectedPost(postId)
        context.res.setBody(post)
    } catch (e: Exception) {
        context.res.setBody(e.message.toString())
    }
}

@Api(routeOverride = "updatepost")
suspend fun updatePost(context: ApiContext) {
    try {
        val updatedPost = context.req.getBody<Post>()
        context.res.setBodyText(
            updatedPost?.let {
                context.data.getValue<MongoDB>().updatePost(it)
            }.toString()
        )
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}
