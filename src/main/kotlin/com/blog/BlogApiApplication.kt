package com.blog

import com.blog.repository.BlogRepository
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogApiApplication(@Autowired val repo: BlogRepository) {

}
fun main(args: Array<String>) {
	runApplication<BlogApiApplication>(*args)
}
