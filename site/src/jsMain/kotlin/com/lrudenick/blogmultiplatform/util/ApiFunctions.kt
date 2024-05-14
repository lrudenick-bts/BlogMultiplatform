package com.lrudenick.blogmultiplatform.util

import com.lrudenick.blogmultiplatform.BuildKonfig
import com.lrudenick.blogmultiplatform.model.ApiListResponse
import com.lrudenick.blogmultiplatform.model.ApiPaths.ADD_POST
import com.lrudenick.blogmultiplatform.model.ApiPaths.CHECK_USER_ID
import com.lrudenick.blogmultiplatform.model.ApiPaths.DELETE_SELECTED_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_LATEST_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_MAIN_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_POPULAR_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_SELECTED_POST
import com.lrudenick.blogmultiplatform.model.ApiPaths.FETCH_SPONSORED_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.SEARCH_POSTS
import com.lrudenick.blogmultiplatform.model.ApiPaths.SEARCH_POSTS_BY_CATEGORY
import com.lrudenick.blogmultiplatform.model.ApiPaths.SUBSCRIBE
import com.lrudenick.blogmultiplatform.model.ApiPaths.UPDATE_POST
import com.lrudenick.blogmultiplatform.model.ApiPaths.USER_CHECK
import com.lrudenick.blogmultiplatform.model.ApiResponse
import com.lrudenick.blogmultiplatform.model.Category
import com.lrudenick.blogmultiplatform.model.Constants.AUTHOR_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.CATEGORY_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.POST_ID_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.QUERY_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.SKIP_PARAM
import com.lrudenick.blogmultiplatform.model.Newsletter
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.model.RandomJoke
import com.lrudenick.blogmultiplatform.model.User
import com.lrudenick.blogmultiplatform.util.Constants.HUMOR_API_URL
import com.lrudenick.blogmultiplatform.util.Id.JOKE
import com.lrudenick.blogmultiplatform.util.Id.JOKE_DATE
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

suspend fun checkUserExistence(user: User): User? {
    return try {
        val result = window.api.tryPost(
            apiPath = USER_CHECK,
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString<User>(it) }
    } catch (e: Exception) {
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = CHECK_USER_ID,
            body = Json.encodeToString(id).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString<Boolean>(it) } ?: false
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun fetchRandomJoke(onComplete: (RandomJoke) -> Unit) {
    val date = localStorage[JOKE_DATE]
    val apiUrl = "$HUMOR_API_URL?api-key=${BuildKonfig.HUMOR_API_KEY}&max-length=180"
    if (date != null) {
        val difference = (Date.now() - date.toDouble())
        val dayHasPassed = difference >= 86400000
        if (dayHasPassed) {
            try {
                val result = window.http.get(apiUrl).decodeToString()
                onComplete(result.parseData())
                localStorage[JOKE_DATE] = Date.now().toString()
                localStorage[JOKE] = result
            } catch (e: Exception) {
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))
                println(e.message)
            }
        } else {
            try {
                localStorage[JOKE]?.parseData<RandomJoke>()?.let { onComplete(it) }
            } catch (e: Exception) {
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))
                println(e.message)
            }
        }
    } else {
        try {
            val result = window.http.get(apiUrl).decodeToString()
            onComplete(result.parseData())
            localStorage[JOKE_DATE] = Date.now().toString()
            localStorage[JOKE] = result
        } catch (e: Exception) {
            onComplete(RandomJoke(id = -1, joke = e.message.toString()))
            println(e.message)
        }
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = ADD_POST,
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun fetchMyPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val author = localStorage[Id.USER_NAME].toString()
        val result = window.api.tryGet(
            apiPath = "fetchmyposts?$SKIP_PARAM=$skip&$AUTHOR_PARAM=$author"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun fetchMainPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = FETCH_MAIN_POSTS)?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchLatestPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result =
            window.api.tryGet(apiPath = "$FETCH_LATEST_POSTS?$SKIP_PARAM=$skip")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchSponsoredPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(apiPath = FETCH_SPONSORED_POSTS)?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchPopularPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result =
            window.api.tryGet(apiPath = "$FETCH_POPULAR_POSTS?$SKIP_PARAM=$skip")?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
    return try {
        window.api.tryPost(
            apiPath = DELETE_SELECTED_POSTS,
            body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun searchPostsByTitle(
    query: String,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "$SEARCH_POSTS?$QUERY_PARAM=$query&$SKIP_PARAM=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun searchPostsByCategory(
    category: Category,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "$SEARCH_POSTS_BY_CATEGORY?$CATEGORY_PARAM=${category.name}&$SKIP_PARAM=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun fetchSelectedPost(id: String): ApiResponse {
    return try {
        val result = window.api.tryGet(
            apiPath = "$FETCH_SELECTED_POST?$POST_ID_PARAM=$id"
        )?.decodeToString()
        if (result != null) {
            val post = result.parseData<Post>()
            ApiResponse.Success(post)
        } else {
            ApiResponse.Error(message = "Result is null")
        }
    } catch (e: Exception) {
        println(e)
        ApiResponse.Error(message = e.message.toString())
    }
}

suspend fun updatePost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = UPDATE_POST,
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun subscribeToNewsletter(newsletter: Newsletter): String {
    return window.api.tryPost(
        apiPath = SUBSCRIBE,
        body = Json.encodeToString(newsletter).encodeToByteArray()
    )?.decodeToString().toString().replace("\"", "")
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}