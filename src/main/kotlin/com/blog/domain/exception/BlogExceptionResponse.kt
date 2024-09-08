package com.blog.domain.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class BlogExceptionResponse<T>(
    val status: HttpStatus,
    val message: T,
    val timestamp: LocalDateTime? = LocalDateTime.now()
){
}