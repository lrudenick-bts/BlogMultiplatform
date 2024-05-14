package com.lrudenick.blogmultiplatform.data

import com.lrudenick.blogmultiplatform.BuildKonfig
import com.lrudenick.blogmultiplatform.model.Category
import com.lrudenick.blogmultiplatform.model.Constants.MAIN_POSTS_LIMIT
import com.lrudenick.blogmultiplatform.model.Constants.POSTS_PER_PAGE
import com.lrudenick.blogmultiplatform.model.Newsletter
import com.lrudenick.blogmultiplatform.model.Post
import com.lrudenick.blogmultiplatform.model.User
import com.lrudenick.blogmultiplatform.util.Constants.DATABASE_NAME
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.serialization.SerializationClassMappingTypeService

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
    private val newsletterCollection = database.getCollection<Newsletter>()

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
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun addPost(post: Post): Boolean {
        return try {
            postCollection.insertOne(post).awaitFirst().wasAcknowledged()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun fetchMyPosts(skip: Int, author: String): List<Post> {
        return try {
            postCollection
                .find(Post::author eq author)
                .sort(descending(Post::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun fetchMainPosts(): List<Post> {
        return try {
            postCollection
                .find(Filters.eq(Post::main.name, true))
                .sort(descending(Post::date))
                .limit(MAIN_POSTS_LIMIT)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun fetchLatestPosts(skip: Int): List<Post> {
        return try {
            postCollection
                .find(
                    Filters.and(
                        Filters.eq(Post::popular.name, false),
                        Filters.eq(Post::main.name, false),
                        Filters.eq(Post::sponsored.name, false)
                    )
                )
                .sort(descending(Post::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun fetchSponsoredPosts(): List<Post> {
        return try {
            postCollection
                .find(Filters.eq(Post::sponsored.name, true))
                .sort(descending(Post::date))
                .limit(2)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun fetchPopularPosts(skip: Int): List<Post> {
        return try {
            postCollection
                .find(Filters.eq(Post::popular.name, true))
                .sort(descending(Post::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return try {
            postCollection
                .deleteMany(Post::id `in` ids)
                .awaitLast()
                .wasAcknowledged()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun searchPostsByTitle(query: String, skip: Int): List<Post> {
        return try {
            val regexQuery = "(?i)$query" // Case insensitive
            val result = postCollection
                .find(Filters.regex(Post::title.name, regexQuery))
                .sort(descending(Post::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
            result
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun searchPostsByCategory(category: Category, skip: Int): List<Post> {
        return try {
            postCollection
                .find(Filters.eq(Post::category.name, category))
                .sort(descending(Post::date))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            emptyList()
        }
    }

    override suspend fun fetchSelectedPost(postId: String): Post? {
        return try {
            postCollection
                .find(Post::id eq postId)
                .awaitFirst()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun updatePost(post: Post): Boolean {
        return try {
            val updates = Updates.combine(
                Updates.set(Post::title.name, post.title),
                Updates.set(Post::subtitle.name, post.subtitle),
                Updates.set(Post::category.name, post.category),
                Updates.set(Post::thumbnail.name, post.thumbnail),
                Updates.set(Post::content.name, post.content),
                Updates.set(Post::main.name, post.main),
                Updates.set(Post::popular.name, post.popular),
                Updates.set(Post::sponsored.name, post.sponsored)
            )
            val result = postCollection
                .updateOne(Post::id eq post.id, updates)
                .awaitFirst()
            result.wasAcknowledged()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun subscribe(newsletter: Newsletter): String {
        val error = "Something went wrong. Please try again later."
        return try {
            val result = newsletterCollection
                .find(Filters.eq(Newsletter::email.name, newsletter.email))
                .toList()
            if (result.isNotEmpty()) {
                "You're already subscribed."
            } else {
                val newEmail = newsletterCollection
                    .insertOne(newsletter)
                    .awaitFirst()
                    .wasAcknowledged()
                if (newEmail) {
                    "Successfully Subscribed!"
                } else {
                    error
                }
            }
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            error
        }
    }
}