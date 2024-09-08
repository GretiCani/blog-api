package com.blog.repository

import com.blog.domain.model.Page

interface DynamoDBRepository<T,ID> {

    fun findAll(limit: Int, lastEvalKey: String?): Page<T>
    fun findById(id: ID): T?
    fun save(entity: T): T?
    fun update(entity: T):T?
    fun delete(id: ID):T?
    fun batchInsert(entities: List<T>)

}