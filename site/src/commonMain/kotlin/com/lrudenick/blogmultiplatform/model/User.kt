package com.lrudenick.blogmultiplatform.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id")
    val id: String = "",
    val username: String = "",
    val password: String = ""
)