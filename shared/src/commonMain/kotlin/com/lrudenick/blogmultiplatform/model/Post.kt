package com.lrudenick.blogmultiplatform.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("_id")
    val _id: String = "",
    val author: String = "",
    val date: Double = 0.0,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String = "",
    val category: Category,
    val popular: Boolean = false,
    val main: Boolean = false,
    val sponsored: Boolean = false
)