package com.lrudenick.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lrudenick.blogmultiplatform.model.Theme
import com.lrudenick.blogmultiplatform.model.User
import com.lrudenick.blogmultiplatform.navigation.Screen
import com.lrudenick.blogmultiplatform.styles.LoginInputStyle
import com.lrudenick.blogmultiplatform.util.Constants.FONT_FAMILY
import com.lrudenick.blogmultiplatform.util.Id
import com.lrudenick.blogmultiplatform.util.Id.REMEMBER
import com.lrudenick.blogmultiplatform.util.Id.USER_ID
import com.lrudenick.blogmultiplatform.util.Id.USER_NAME
import com.lrudenick.blogmultiplatform.util.Res
import com.lrudenick.blogmultiplatform.util.checkUserExistence
import com.lrudenick.blogmultiplatform.util.noBorder
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set

@Page
@Composable
fun LoginPage() {
    var errorText by remember { mutableStateOf(" ") }
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(leftRight = 50.px, top = 80.px, bottom = 24.px)
                .backgroundColor(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .margin(bottom = 50.px)
                    .width(100.px),
                src = Res.Image.LOGO,
                description = "Logo Image"
            )

            Input(
                type = InputType.Text,
                attrs = LoginInputStyle.toModifier()
                    .id(Id.USER_NAME_INPUT)
                    .margin(bottom = 12.px)
                    .toAttrs {
                        attr("placeholder", "Username")
                    }
            )

            Input(
                type = InputType.Password,
                attrs = LoginInputStyle.toModifier()
                    .id(Id.PASSWORD_INPUT)
                    .margin(bottom = 20.px)
                    .toAttrs {
                        attr("placeholder", "Password")
                    }
            )

            Button(
                attrs = Modifier
                    .margin(bottom = 24.px)
                    .width(350.px)
                    .height(50.px)
                    .backgroundColor(Theme.Primary.rgb)
                    .color(Colors.White)
                    .borderRadius(r = 4.px)
                    .fontFamily(FONT_FAMILY)
                    .fontWeight(FontWeight.Medium)
                    .fontSize(14.px)
                    .noBorder()
                    .cursor(Cursor.Pointer)
                    .onClick {
                        scope.launch {
                            val username =
                                (document.getElementById(Id.USER_NAME_INPUT) as HTMLInputElement).value
                            val password =
                                (document.getElementById(Id.PASSWORD_INPUT) as HTMLInputElement).value
                            if (username.isNotEmpty() && password.isNotEmpty()) {
                                val user = checkUserExistence(
                                    User(
                                        username = username,
                                        password = password
                                    )
                                )

                                if (user != null) {
                                    rememberLoggedIn(true, user)
                                    context.router.navigateTo(Screen.AdminHome.route)
                                } else {
                                    errorText = "The user doesn't exist."
                                    delay(3_000)
                                    errorText = " "
                                }
                            } else {
                                errorText = "Inputs fields are empty."
                                delay(3_000)
                                errorText = " "
                            }
                        }
                    }
                    .toAttrs()
            ) {
                SpanText("Sign in")
            }
            SpanText(
                modifier = Modifier
                    .width(350.px)
                    .color(Colors.Red)
                    .fontFamily(FONT_FAMILY)
                    .textAlign(TextAlign.Center),
                text = errorText
            )
        }
    }
}

private fun rememberLoggedIn(
    remember: Boolean,
    user: User? = null
) {
    localStorage[REMEMBER] = remember.toString()
    user?.let {
        localStorage[USER_ID] = it.id
        localStorage[USER_NAME] = it.username
    }
}