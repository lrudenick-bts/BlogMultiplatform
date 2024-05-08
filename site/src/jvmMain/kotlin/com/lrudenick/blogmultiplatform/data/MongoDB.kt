package com.lrudenick.blogmultiplatform.data

import com.lrudenick.blogmultiplatform.util.Constants.DATABASE_NAME
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import com.lrudenick.blogmultiplatform.BuildKonfig
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.model.User

@InitApi
fun initMongoDB(context: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.mapping.service",
        SerializationClassMappingTypeService::class.qualifiedName!!
    )
    context.data.add(MongoDB(context))

}

class MongoDB(private val context: InitApiContext) : MongoRepository {
    // Replace the placeholder with your Atlas connection string
    private val uri = BuildKonfig.MDB_CONNECTION_URI

    // Construct a ServerApi instance using the ServerApi.builder() method
    private val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    private val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(uri))
        .serverApi(serverApi)
        .build()
    private val client = KMongo.createClient(settings)
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>()
    private val postCollection = database.getCollection<Post>()

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection.find(
                and(
                    User::username eq user.username,
                    User::password eq user.password
                )
            ).awaitFirst()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(User::id eq id).awaitFirst()
            documentCount > 0
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addPost(post: Post): Boolean {
        return try {
            return postCollection.insertOne(post).awaitFirst().wasAcknowledged()
        } catch (e: Exception) {
            false
        }
    }
}