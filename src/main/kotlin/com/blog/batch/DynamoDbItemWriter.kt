package com.blog.batch

import com.blog.repository.DynamoDBRepository
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

open class DynamoDbItemWriter<T,ID>: ItemWriter<T> {

    private lateinit var repository : DynamoDBRepository<T,ID>

    override fun write(chunk: Chunk<out T>) {
        repository.batchInsert(chunk.items)
    }

    fun setRepository(repository: DynamoDBRepository<T,ID>) {
        this.repository = repository
    }
}