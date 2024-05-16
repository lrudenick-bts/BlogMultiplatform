package com.lrudenick.blogmultiplatform.androidapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lrudenick.blogmultiplatform.androidapp.components.NavigationDrawer
import com.lrudenick.blogmultiplatform.androidapp.components.PostCardsView
import com.lrudenick.blogmultiplatform.androidapp.model.Post
import com.lrudenick.blogmultiplatform.androidapp.util.RequestState
import com.lrudenick.blogmultiplatform.model.Category
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: RequestState<List<Post>>,
    searchedPosts: RequestState<List<Post>>,
    query: String,
    searchBarOpened: Boolean,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onSearchBarChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onPostClick: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        drawerState = drawerState,
        onCategorySelect = { category ->
            scope.launch {
                onCategorySelect(category)
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Blog") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = "Drawer icon"
                            )
                        }

                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchBarChange(true)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
                if (searchBarOpened) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch,
                        active = active,
                        onActiveChange = onActiveChange,
                        placeholder = { Text(text = "Search here...") },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    onSearchBarChange(false)
                                    onActiveChange(true)
                                }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = "Back arrow",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = {
                                    onQueryChange("")
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    ) {
                        PostCardsView(
                            posts = searchedPosts,
                            topMargin = 12.dp,
                            onPostClick = onPostClick,
                            onCategorySelect = onCategorySelect
                        )
                    }
                }
            }
        ) {
            PostCardsView(
                posts = posts,
                topMargin = it.calculateTopPadding(),
                hideMessage = true,
                onPostClick = onPostClick,
                onCategorySelect = onCategorySelect
            )
        }
    }
}