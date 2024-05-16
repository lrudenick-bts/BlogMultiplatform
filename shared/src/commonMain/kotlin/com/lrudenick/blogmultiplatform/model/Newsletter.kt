package com.lrudenick.blogmultiplatform.model

import kotlinx.serialization.Serializable

@Serializable
data class Newsletter(
    val email: String
)