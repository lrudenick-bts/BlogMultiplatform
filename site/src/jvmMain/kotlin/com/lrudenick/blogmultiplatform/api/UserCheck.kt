package com.lrudenick.blogmultiplatform.api

import com.lrudenick.blogmultiplatform.data.MongoDB
import com.lrudenick.blogmultiplatform.model.ApiPaths.CHECK_USER_ID
import com.lrudenick.blogmultiplatform.model.ApiPaths.USER_CHECK
import com.lrudenick.blogmultiplatform.model.User
import com.lrudenick.blogmultiplatform.util.getBody
import com.lrudenick.blogmultiplatform.util.setBody
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import java.security.MessageDigest

@Api(routeOverride = USER_CHECK)
suspend fun userCheck(context: ApiContext) {
    try {

        val userRequest = context.req.getBody<User>()
        val user = userRequest?.let {
            context.data.getValue<MongoDB>()
                .checkUserExistence(it.copy(password = hashPassword(it.password)))
        }

        if (user != null) {
            context.res.setBody(User(_id = user._id, username = user.username))
        } else {
            context.res.setBody(Exception("User doesn't exist."))
        }
    } catch (e: Exception) {
        context.res.setBody(e)
    }
}


@Api(routeOverride = CHECK_USER_ID)
suspend fun checkUserId(context: ApiContext) {
    try {
        val idRequest = context.req.getBody<String>()

        val result = idRequest?.let {
            context.data.getValue<MongoDB>().checkUserId(it)
        }
        context.res.setBody(result ?: false)
    } catch (e: Exception) {
        context.res.setBody(false)
    }
}

private fun hashPassword(password: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(password.toByteArray())
    val hexString = StringBuffer()

    for (byte in hashBytes) {
        hexString.append(String.format("%02x", byte))
    }
    return hexString.toString()
}