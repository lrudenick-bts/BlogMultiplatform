package com.lrudenick.blogmultiplatform.androidapp.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lrudenick.blogmultiplatform.androidapp.navigation.Screen
import com.lrudenick.blogmultiplatform.androidapp.screens.details.DetailsScreen
import com.lrudenick.blogmultiplatform.androidapp.util.Constants.POST_ID_ARGUMENT
import com.lrudenick.blogmultiplatform.Constants.SHOW_SECTIONS_PARAM

fun NavGraphBuilder.detailsRoute(
    onBackPress: () -> Unit
) {
    composable(
        route = Screen.Details.route,
        arguments = listOf(navArgument(name = POST_ID_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val postId = it.arguments?.getString(POST_ID_ARGUMENT)
        DetailsScreen(
            url = "http://192.168.0.17:8080/posts/post?$POST_ID_ARGUMENT=$postId&$SHOW_SECTIONS_PARAM=false",
            onBackPress = onBackPress
        )
    }
}