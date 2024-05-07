package com.lrudenick.blogmultiplatform.navigation

sealed class Screen(val route: String) {
    data object AdminHome: Screen("/admin/")
    data object AdminLogin: Screen("/admin/login")
    data object AdminCreate: Screen("/admin/create")
    data object AdminMyPosts: Screen("/admin/myposts")
}