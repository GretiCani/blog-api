package com.blog.controller

import com.blog.service.impl.BatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/batch")
class BatchController(@Autowired private val batchService: BatchService)
{

    @GetMapping("/blogs")
    fun blogUploadEvent(@RequestParam fileName: String): ResponseEntity<Unit>{
        batchService.launchBlogUpload(fileName)
        return ResponseEntity.ok().build()
    }
}