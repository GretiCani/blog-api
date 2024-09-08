package com.blog.repository

import com.blog.domain.entity.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException



@Repository
class BlogRepository(@Autowired private val dynamoDbClient: DynamoDbEnhancedClient,
                     @Autowired private val blogPostTable: DynamoDbTable<Post> ):
    AbstractDynamoDBRepository<Post, String>(Post::class.java) {

    override fun getDynamoDBClient(): DynamoDbEnhancedClient = dynamoDbClient

    override fun getTableName(): DynamoDbTable<Post> = blogPostTable

    override fun save(entity: Post): Post? = entity.takeIf { isUniqueValue(it) }?.let { super.save(entity)}

    override fun batchInsert(entities: List<Post>) {

        if (entities.any(::isUniqueValue))
            super.batchInsert(entities)
    }
    fun isUniqueValue(post: Post): Boolean = try {
        val index = getTableName().index("friendlyUrl-index")
        val queryConditional = QueryConditional.keyEqualTo { it.partitionValue(post.friendlyUrl) }
        index.query{r -> r.queryConditional(queryConditional)}
            .flatMap { it.items() }.isEmpty()
    } catch (e: DynamoDbException) {
        println("Failed to query the index: ${e.message}")
        throw e
    }

}