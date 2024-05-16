package com.lrudenick.blogmultiplatform.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lrudenick.blogmultiplatform.model.Category
import com.lrudenick.blogmultiplatform.model.ControlStyle
import com.lrudenick.blogmultiplatform.model.EditorControl
import com.lrudenick.blogmultiplatform.model.Theme
import com.lrudenick.blogmultiplatform.navigation.Screen
import com.lrudenick.blogmultiplatform.util.Id.REMEMBER
import com.lrudenick.blogmultiplatform.util.Id.USER_ID
import com.lrudenick.blogmultiplatform.util.Id.USER_NAME
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import kotlin.js.Date

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

fun Modifier.noBorder(): Modifier {
    return this.border(
        width = 0.px,
        style = LineStyle.None,
        color = Colors.Transparent
    ).outline(
        width = 0.px,
        style = LineStyle.None,
        color = Colors.Transparent
    )
}

fun getEditor() = document.getElementById(Id.EDITOR) as HTMLTextAreaElement

fun getSelectedIntRange(): IntRange? {
    val editor = getEditor()
    val start = editor.selectionStart
    val end = editor.selectionEnd
    return if (start != null && end != null) {
        IntRange(start, (end - 1))
    } else null
}

fun getSelectedText(): String? {
    val range = getSelectedIntRange()
    return if (range != null) {
        getEditor().value.substring(range)
    } else null
}

fun applyStyle(controlStyle: ControlStyle) {
    val selectedText = getSelectedText()
    val selectedIntRange = getSelectedIntRange()
    if (selectedIntRange != null && selectedText != null) {
        getEditor().value = getEditor().value.replaceRange(
            range = selectedIntRange,
            replacement = controlStyle.style
        )
        document.getElementById(Id.EDITOR_PREVIEW)?.innerHTML = getEditor().value
    }
}

fun applyControlStyle(
    editorControl: EditorControl,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit
) {
    when (editorControl) {
        EditorControl.Bold -> {
            applyStyle(
                ControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Italic -> {
            applyStyle(
                ControlStyle.Italic(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Link -> {
            onLinkClick()
        }

        EditorControl.Title -> {
            applyStyle(
                ControlStyle.Title(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Subtitle -> {
            applyStyle(
                ControlStyle.Subtitle(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Quote -> {
            applyStyle(
                ControlStyle.Quote(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Code -> {
            applyStyle(
                ControlStyle.Code(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Image -> {
            onImageClick()
        }
    }
}

fun Long.parseDateString() = Date(this).toLocaleDateString()

fun parseSwitchText(posts: List<String>): String {
    return if (posts.size == 1) "1 Post Selected" else "${posts.size} Posts Selected"
}

fun validateEmail(email: String): Boolean {
    val regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return regex.toRegex().matches(email)
}

val Category.color: String
    get() = when (this) {
        Category.Technology -> Theme.Green.hex
        Category.Programming -> Theme.Yellow.hex
        Category.Design -> Theme.Purple.hex
    }
