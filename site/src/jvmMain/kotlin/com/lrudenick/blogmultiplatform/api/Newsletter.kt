package com.lrudenick.blogmultiplatform.api

import com.lrudenick.blogmultiplatform.data.MongoDB
import com.lrudenick.blogmultiplatform.model.ApiPaths.SUBSCRIBE
import com.lrudenick.blogmultiplatform.model.Newsletter
import com.lrudenick.blogmultiplatform.util.getBody
import com.lrudenick.blogmultiplatform.util.setBody
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue

@Api(routeOverride = SUBSCRIBE)
suspend fun subscribeNewsletter(context: ApiContext) {
    try {
        val newsletter = context.req.getBody<Newsletter>()
        context.res.setBody(newsletter?.let {
            context.data.getValue<MongoDB>().subscribe(newsletter = it)
        })
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}