package com.lrudenick.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.lrudenick.blogmultiplatform.components.AdminPageLayout
import com.lrudenick.blogmultiplatform.components.SidePanel
import com.lrudenick.blogmultiplatform.util.Constants
import com.lrudenick.blogmultiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn { CreateScreen() }
}

@Composable
fun CreateScreen() {
    AdminPageLayout {

    }
}