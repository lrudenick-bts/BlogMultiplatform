package com.lrudenick.blogmultiplatform.androidapp.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lrudenick.blogmultiplatform.androidapp.data.MongoSync
import com.lrudenick.blogmultiplatform.androidapp.model.Post
import com.lrudenick.blogmultiplatform.androidapp.util.Constants.APP_ID
import com.lrudenick.blogmultiplatform.androidapp.util.RequestState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val _allPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts
    private val _searchedPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val searchedPosts: State<RequestState<List<Post>>> = _searchedPosts

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _allPosts.value = RequestState.Loading
                }
                App.create(APP_ID).login(Credentials.anonymous())
                fetchAllPosts()
            } catch (e: Exception) {
                _allPosts.value = RequestState.Error(e)
            }
        }
    }

    private suspend fun fetchAllPosts() {
        MongoSync.readAllPosts().collectLatest {
            _allPosts.value = it
        }
    }

    fun searchPostsByTitle(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _searchedPosts.value = RequestState.Loading
            }
            MongoSync.searchPostsByTitle(query = query).collectLatest {
                _searchedPosts.value = it
            }
        }
    }

    fun resetSearchedPosts() {
        _searchedPosts.value = RequestState.Idle
    }
}