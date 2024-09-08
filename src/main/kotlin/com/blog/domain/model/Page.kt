package com.blog.domain.model

data class Page<T> (
    val lastEvalKey: String? = null,
    val pageSize: Int = 5,
    val data: List<T> = emptyList()
    ){}