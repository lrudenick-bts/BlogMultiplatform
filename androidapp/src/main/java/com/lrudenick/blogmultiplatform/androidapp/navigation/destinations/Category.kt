package com.lrudenick.blogmultiplatform.androidapp.navigation.destinations

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lrudenick.blogmultiplatform.androidapp.navigation.Screen
import com.lrudenick.blogmultiplatform.androidapp.screens.category.CategoryScreen
import com.lrudenick.blogmultiplatform.androidapp.screens.category.CategoryViewModel
import com.lrudenick.blogmultiplatform.androidapp.util.Constants.CATEGORY_ARGUMENT
import com.lrudenick.blogmultiplatform.model.Category

fun NavGraphBuilder.categoryRoute(
    onBackPress: () -> Unit,
    onPostClick: (String) -> Unit
) {
    composable(
        route = Screen.Category.route,
        arguments = listOf(navArgument(name = CATEGORY_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val viewModel: CategoryViewModel = viewModel()
        val selectedCategory =
            it.arguments?.getString(CATEGORY_ARGUMENT) ?: Category.Programming.name
        CategoryScreen(
            posts = viewModel.categoryPosts.value,
            category = Category.valueOf(selectedCategory),
            onBackPress = onBackPress,
            onPostClick = onPostClick
        )
    }
}