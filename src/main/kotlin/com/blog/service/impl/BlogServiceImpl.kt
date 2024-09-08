package com.blog.service.impl

import com.blog.domain.entity.Post
import com.blog.domain.exception.ResourceNotFoundException
import com.blog.domain.model.BlogPost
import com.blog.domain.model.Page
import com.blog.repository.BlogRepository
import com.blog.service.BlogService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
@Service
class BlogServiceImpl(
    @Autowired private val blogRepository: BlogRepository
) : BlogService {

    override fun findAll(limit: Int, lastEvaluatedKey: String?): Page<BlogPost> {
        val entityPage = blogRepository.findAll(limit, lastEvaluatedKey)
        val resultData =  entityPage.data.asSequence().map { entity ->
            BlogPost(id=entity.id, title = entity.title, friendlyUrl = entity.friendlyUrl, content = entity.content)
        }.toList()
        return Page(entityPage.lastEvalKey,entityPage.pageSize,resultData)
    }

    override fun create(@Valid bp: BlogPost): BlogPost? {
        val entity = Post(title = bp.title, friendlyUrl = bp.friendlyUrl, content = bp.content)
        val resp = blogRepository.save(entity)
        resp?:run { throw ResourceNotFoundException("Failed to create blog post. The friendlyUrl '${bp.friendlyUrl}' already exists.")  }
        return toDto(resp)
    }

    override fun update(@Valid bp: BlogPost): BlogPost {
        val entity = blogRepository.findById(bp.id!!)
        entity?.let {
            it.title = bp.title
            it.friendlyUrl = bp.friendlyUrl
            it.content = bp.content
        }?:run { throw ResourceNotFoundException("Failed to update blog post with ID ${bp.id}: Entity does not exist") }
        val updated = blogRepository.update(entity)
        return toDto(updated)
    }

    override fun delete(id: String) {
        blogRepository.delete(id)?:run { throw ResourceNotFoundException("Failed to delete blog post with ID $id: Entity does not exist") }
    }
}


fun toDto(en: Post?): BlogPost = BlogPost(en?.id, en?.title?:"", en?.content?:"", en?.friendlyUrl?:"")
