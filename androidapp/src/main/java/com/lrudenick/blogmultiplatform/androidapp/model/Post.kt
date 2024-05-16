package com.lrudenick.blogmultiplatform.androidapp.model

import com.lrudenick.blogmultiplatform.model.Category
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey

@PersistedName("post")
open class Post : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var author: String = ""
    var date: Double = 0.0
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var category: String = Category.Programming.name
}