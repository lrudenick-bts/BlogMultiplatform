package com.lrudenick.blogmultiplatform.util

import com.lrudenick.blogmultiplatform.BuildKonfig
import com.lrudenick.blogmultiplatform.model.RandomJoke
import com.lrudenick.blogmultiplatform.model.User
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
            apiPath = "usercheck",
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
            apiPath = "checkuserid",
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
    val apiUrl = "${Constants.HUMOR_API_URL}?api-key=${BuildKonfig.HUMOR_API_KEY}&max-length=180"
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

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}