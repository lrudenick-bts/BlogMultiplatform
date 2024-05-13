package com.lrudenick.blogmultiplatform.navigation

import com.lrudenick.blogmultiplatform.model.Constants.POST_ID_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.QUERY_PARAM
import com.lrudenick.blogmultiplatform.model.Constants.UPDATED_PARAM


sealed class Screen(val route: String) {
    data object AdminHome: Screen("/admin/")
    data object AdminLogin: Screen("/admin/login")
    data object AdminCreate: Screen("/admin/create") {
        fun passPostId(id: String) = "/admin/create?${POST_ID_PARAM}=$id"
    }
    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String) = "/admin/myposts?${QUERY_PARAM}=$query"
    }
    data object AdminSuccess: Screen("/admin/success") {
        fun postUpdated() = "/admin/success?${UPDATED_PARAM}=true"
    }
}