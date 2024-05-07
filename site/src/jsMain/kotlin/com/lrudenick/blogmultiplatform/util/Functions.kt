package com.lrudenick.blogmultiplatform.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lrudenick.blogmultiplatform.navigation.Screen
import com.lrudenick.blogmultiplatform.util.Id.REMEMBER
import com.lrudenick.blogmultiplatform.util.Id.USER_ID
import com.lrudenick.blogmultiplatform.util.Id.USER_NAME
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.localStorage
import org.w3c.dom.get

@Composable
fun isUserLoggedIn(content: @Composable () -> Unit) {
    val context = rememberPageContext()
    val remembered = remember { localStorage[REMEMBER].toBoolean() }
    val userId = remember { localStorage[USER_ID] }
    var userIdExists by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userIdExists = !userId.isNullOrEmpty() && checkUserId(userId)

        if (!remembered || !userIdExists) {
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }

    if (remembered && userIdExists) {
        content()
    } else {
        println("Loading ...")
    }
}

fun logout() {
    localStorage.removeItem(REMEMBER)
    localStorage.removeItem(USER_ID)
    localStorage.removeItem(USER_NAME)
}