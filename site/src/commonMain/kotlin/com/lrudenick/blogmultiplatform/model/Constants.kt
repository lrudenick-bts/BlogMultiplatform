package com.lrudenick.blogmultiplatform.model

object Constants {
    const val POSTS_PER_PAGE = 8
    const val MAIN_POSTS_LIMIT = 4

    const val SKIP_PARAM = "skip"
    const val AUTHOR_PARAM = "author"
    const val QUERY_PARAM = "query"
    const val POST_ID_PARAM = "postId"
    const val UPDATED_PARAM = "updated"
    const val CATEGORY_PARAM = "category"
}

object ApiPaths {
    const val USER_CHECK = "usercheck"
    const val CHECK_USER_ID = "checkuserid"
    const val ADD_POST = "addpost"
    const val FETCH_MY_POSTS = "fetchmyposts"
    const val FETCH_MAIN_POSTS = "fetchmainposts"
    const val FETCH_LATEST_POSTS = "fetchlatestposts"
    const val FETCH_SPONSORED_POSTS = "fetchsponsoredposts"
    const val FETCH_POPULAR_POSTS = "fetchpopularposts"
    const val DELETE_SELECTED_POSTS = "deleteselectedposts"
    const val SEARCH_POSTS = "searchposts"
    const val SEARCH_POSTS_BY_CATEGORY = "searchpostsbycategory"
    const val FETCH_SELECTED_POST = "fetchselectedpost"
    const val UPDATE_POST = "updatepost"
    const val SUBSCRIBE = "subscribe"

}