package com.blog.controller.advice

import com.blog.domain.exception.BlogExceptionResponse
import com.blog.domain.exception.NotUniqueException
import com.blog.domain.exception.ResourceNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class BlogControllerAdvice : ResponseEntityExceptionHandler() {


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleMethodArgumentNotValid(ex: ConstraintViolationException
    ): ResponseEntity<Any> = ResponseEntity.badRequest().body(BlogExceptionResponse(HttpStatus.BAD_REQUEST, getRequiredFields(ex)))

    @ExceptionHandler(NotUniqueException::class)
    fun handleNotUniqueException(ex: NotUniqueException
    ): ResponseEntity<Any> = ResponseEntity.badRequest().body(BlogExceptionResponse(HttpStatus.BAD_REQUEST,ex.message ))

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException
    ): ResponseEntity<Any> = ResponseEntity.badRequest().body(BlogExceptionResponse(HttpStatus.BAD_REQUEST,ex.message ))


    private fun getRequiredFields(ex: ConstraintViolationException): List<String> {
        val fieldMap : MutableList<String> = mutableListOf()
        println("this is message ${ex.message}")
        ex.constraintViolations.forEach {fieldMap.add(it.message)}
        return fieldMap
    }




}