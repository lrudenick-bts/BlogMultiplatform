package com.lrudenick.blogmultiplatform.model

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(val color: String) {
    Technology(Theme.Green.hex),
    Programming(Theme.Yellow.hex),
    Design(Theme.Purple.hex)
}