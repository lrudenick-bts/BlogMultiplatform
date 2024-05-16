package com.lrudenick.blogmultiplatform.api

import com.lrudenick.blogmultiplatform.data.MongoDB
import com.lrudenick.blogmultiplatform.model.ApiListResponse
import com.lrudenick.blogmultiplatform.model.ApiPaths.ADD_POST
import com.lrudenick.blogmultiplatform.model.ApiPaths.DELETE_SELECTED_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_LATEST_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_MAIN_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_MY_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_POPULAR_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_SELECTED_POST
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_SPONSORED_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.SEARCH_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.SEARCH_POSTS_BY_CATEGORY
import com.lrudenick.blogmultiplatform.model.ApiPaths.UPDATE_POST
import com.lrudenick.blogmultiplatform.model.Category
import com.lrudenick.blogmultiplatform.model.Constants.AUTHOR_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.CATEGORY_PARAM
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
import org.bson.codecs.ObjectIdGenerator

@Api(routeOverride = ADD_POST)
suspend fun addPost(context: ApiContext) {
    try {
        val post = context.req.getBody<Post>()
        val newPost = post?.copy(_id = ObjectIdGenerator().generate().toString())
        val result = newPost?.let {
            context.data.getValue<MongoDB>().addPost(newPost)
        } ?: false
        context.res.setBodyText(result.toString())
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}

@Api(routeOverride = FETCH_MY_POSTS)
suspend fun fetchMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val author = context.req.params[AUTHOR_PARAM] ?: ""
        val posts = context.data.getValue<MongoDB>().fetchMyPosts(skip, author)
        context.res.setBody(ApiListResponse.Success(data = posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(e.message.toString()))
    }
}

@Api(routeOverride = FETCH_MAIN_POSTS)
suspend fun fetchMainPosts(context: ApiContext) {
    try {
        val mainPosts = context.data.getValue<MongoDB>().fetchMainPosts()
        context.res.setBody(ApiListResponse.Success(data = mainPosts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = FETCH_LATEST_POSTS)
suspend fun fetchLatestPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val latestPosts = context.data.getValue<MongoDB>().fetchLatestPosts(skip = skip)
        context.res.setBody(ApiListResponse.Success(data = latestPosts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = FETCH_SPONSORED_POSTS)
suspend fun fetchSponsoredPosts(context: ApiContext) {
    try {
        val sponsoredPosts = context.data.getValue<MongoDB>().fetchSponsoredPosts()
        context.res.setBody(ApiListResponse.Success(data = sponsoredPosts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = FETCH_POPULAR_POSTS)
suspend fun fetchPopularPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val popularPosts = context.data.getValue<MongoDB>().fetchPopularPosts(skip = skip)
        context.res.setBody(ApiListResponse.Success(data = popularPosts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = DELETE_SELECTED_POSTS)
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

@Api(routeOverride = SEARCH_POSTS)
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

@Api(routeOverride = SEARCH_POSTS_BY_CATEGORY)
suspend fun searchPostsByCategory(context: ApiContext) {
    try {
        val category =
            Category.valueOf(context.req.params[CATEGORY_PARAM] ?: Category.Programming.name)
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val posts = context.data.getValue<MongoDB>().searchPostsByCategory(
            category = category,
            skip = skip
        )
        context.res.setBody(ApiListResponse.Success(data = posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = FETCH_SELECTED_POST)
suspend fun fetchSelectedPost(context: ApiContext) {
    try {
        val postId = context.req.params[POST_ID_PARAM] ?: ""
        val post = context.data.getValue<MongoDB>().fetchSelectedPost(postId)
        context.res.setBody(post)
    } catch (e: Exception) {
        context.res.setBody(e.message.toString())
    }
}

@Api(routeOverride = UPDATE_POST)
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
