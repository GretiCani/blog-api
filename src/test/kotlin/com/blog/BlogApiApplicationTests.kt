package com.blog

import com.blog.domain.entity.Post
import com.blog.repository.BlogRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class BlogApiApplicationTests{

	@Autowired
	private lateinit var blogRepository: BlogRepository

	@Test
	fun contextLoads() {
		val entity = Post(id=UUID.randomUUID().toString(),"First",content = "Content", friendlyUrl = "friendly")
		val output = blogRepository.save(entity)
		Assertions.assertNotNull(output?.id)
	}

}
