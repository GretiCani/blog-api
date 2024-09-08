package com.blog.service

import com.blog.domain.model.BlogPost
import com.blog.domain.model.Page
import jakarta.validation.Valid

interface BlogService {

    fun findAll(limit: Int , lastEvaluatedKey: String? = null): Page<BlogPost>
    fun create(@Valid bp: BlogPost): BlogPost?
    fun update(@Valid bp: BlogPost): BlogPost
    fun delete(id: String)

}