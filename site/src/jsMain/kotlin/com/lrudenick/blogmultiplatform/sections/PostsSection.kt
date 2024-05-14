package com.lrudenick.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.lrudenick.blogmultiplatform.components.PostsView
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun PostsSection(
    breakpoint: Breakpoint,
    posts: List<Post>,
    title: String? = null,
    showMoreVisibility: Boolean,
    onShowMore: () -> Unit,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .margin(topBottom = 50.px)
            .maxWidth(PAGE_WIDTH.px),
        contentAlignment = Alignment.TopCenter
    ) {
        PostsView(
            breakpoint = breakpoint,
            posts = posts,
            title = title,
            showMoreVisibility = showMoreVisibility,
            onShowMore = onShowMore,
            onClick = onClick
        )
    }
}