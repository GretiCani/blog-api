package com.blog.repository

import com.blog.domain.model.Page
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

abstract class AbstractDynamoDBRepository<T,ID>(private val clazz: Class<T>) : DynamoDBRepository<T, ID> {

    protected abstract fun getDynamoDBClient(): DynamoDbEnhancedClient
    protected abstract fun getTableName(): DynamoDbTable<T>

    override fun findAll(limit: Int, lastEvalKey: String?): Page<T> {
        var lastEvaluatedKey: Map<String, AttributeValue>? = null
        lastEvalKey?.let { lastEvaluatedKey = mapOf("id" to AttributeValue.builder().s(lastEvalKey).build()) }
        val scanRequest = ScanEnhancedRequest.builder().limit(limit).exclusiveStartKey(lastEvaluatedKey).build()
        val page = getTableName().scan(scanRequest).first()
        return Page(pageSize = limit, data = page.items(), lastEvalKey = page.lastEvaluatedKey()?.get("id")?.s())
    }

    override fun findById(id: ID): T? {
       return getTableName().getItem(Key.builder().partitionValue(id.toString()).build())
    }

    override fun save(entity: T): T?= getTableName().updateItem(entity)
    override fun update(entity: T):T?= getTableName().updateItem(entity)
    override fun delete(id: ID):T? = getTableName().deleteItem(Key.builder().partitionValue(id.toString()).build())


    override fun batchInsert(entities: List<T>) {
        val writeBatch= WriteBatch.builder(clazz).mappedTableResource(getTableName())
        entities.forEach { writeBatch.addPutItem{ builder -> builder.item(it)} }
        val batchWriteItemRequest = BatchWriteItemEnhancedRequest.builder()
            .writeBatches(writeBatch.build()).build()
        getDynamoDBClient().batchWriteItem(batchWriteItemRequest)

    }
}