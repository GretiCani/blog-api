package com.blog.controller

import com.blog.domain.model.BlogPost
import com.blog.domain.model.Page
import com.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/api/blogs")
class BlogController (@Autowired private val blogService: BlogService){

    @GetMapping
    fun getBlogs(@RequestParam(value = "limit", defaultValue = "5", required = false) limit: Int,
                 @RequestParam(value = "lastEvaluatedKey", required = false) lastEvaluatedKey: String?):
            Page<BlogPost> = blogService.findAll(limit,lastEvaluatedKey)


    @PostMapping
    fun addBlog(@RequestBody blogPost: BlogPost): ResponseEntity<BlogPost> = ResponseEntity.ok(blogService.create(blogPost))

    @PutMapping("/{id}")
    fun updateBlog(@PathVariable id : String,@RequestBody blogPost: BlogPost): ResponseEntity<BlogPost>{
        blogPost.id=id
        val blog = blogService.update(blogPost)
        return ResponseEntity.ok(blog)
    }

    @DeleteMapping("/{id}")
    fun deleteBlog(@PathVariable id: String): ResponseEntity<Unit>{
        blogService.delete(id)
        return ResponseEntity.ok().build()
    }

}