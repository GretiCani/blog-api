package com.blog.batch

import com.blog.domain.entity.Post
import com.blog.domain.model.BlogPost
import org.springframework.batch.item.ItemProcessor

class BlogPostProcessor : ItemProcessor<BlogPost, Post> {

    override fun process(item: BlogPost): Post {
        val output: Post = Post(title= item.title,friendlyUrl = item.friendlyUrl, content = item.content, createdBy = "BATCH")
        return output
    }
}