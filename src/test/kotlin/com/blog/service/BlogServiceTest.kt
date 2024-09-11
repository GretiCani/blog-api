package com.blog.service

import com.blog.domain.model.BlogPost
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.http.MediaType

@SpringBootTest
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
class BlogServiceTest {

    @Autowired lateinit var mockMvc: MockMvc

    companion object {
        var lastEvaluatedKey:String?=null
        var postIdCreated:String?=null
    }

    @Nested
    @Order(1)
    @DisplayName("Should return a page of blog posts. GET: /v1/api/blogs")
    @TestMethodOrder(OrderAnnotation::class)
    inner class FindAllBlogsTest {
        @Test
        @Order(1)
        fun `When findAll is called with defaults then return page with default size 5`(){
            val resp =mockMvc.perform(get("/v1/api/blogs")).andDo(print())
                .andExpect(status().isOk)
                .andExpectAll(
                    jsonPath("$.lastEvalKey").isNotEmpty,
                    jsonPath("$.pageSize").value(5),
                    jsonPath("$.data").isNotEmpty)
                .andReturn()
            lastEvaluatedKey  = JsonPath.parse(resp.response.contentAsString).read<String>("$.lastEvalKey")
        }

        @Test
        @Order(2)
        fun `When findAll is called with limit=10 and lastEvalKey then return required page with size 10`(){
            mockMvc.perform(get("/v1/api/blogs?limit={limit}&lastEvalKey={lastEvalKey}",10,lastEvaluatedKey)).andDo(print())
                .andExpect(status().isOk)
                .andExpectAll(
                    jsonPath("$.lastEvalKey").isNotEmpty,
                    jsonPath("$.pageSize").value(10),
                    jsonPath("$.data").isNotEmpty)
        }

    }

    @Nested
    @Order(2)
    @DisplayName("Should create blog post. POST: /v1/api/blogs")
    inner class CreateBlogTest {

        @Test
        fun `When BlogPost friendlyUrl is unique then create BlogPost`() {
            val post = BlogPost(title = "Title", friendlyUrl = "friendlyUrl", content = "content")
            val resp = mockMvc.perform(
                post("/v1/api/blogs").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(post))
            )
                .andDo(print()).andExpect(status().isOk)
                .andExpectAll(
                    jsonPath("$.id").isNotEmpty,
                    jsonPath("$.title").value(post.title),
                    jsonPath("$.friendlyUrl").value(post.friendlyUrl),
                    jsonPath("$.content").value(post.content),
                ).andReturn()
            postIdCreated = JsonPath.parse(resp.response.contentAsString).read<String>("$.id")
        }

        @Test
        fun `When BlogPost friendlyUrl is not unique then return BAD_REQUEST`() {
            val post = BlogPost(title = "Title", friendlyUrl = "friendlyUrl", content = "content")
            mockMvc.perform(
                post("/v1/api/blogs").accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(post))
            )
                .andDo(print()).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        }

    }
        @Nested
        @Order(3)
        @DisplayName("Should update blog post for a given id. PUT: /v1/api/blogs/{id}")
        inner class UpdateBlogTest {

            @Test
            fun `When BlogPost id exist then update BlogPost with given values`() {
                val post =
                    BlogPost(title = "Title updated", friendlyUrl = "friendlyUrl updated", content = "content updated")
                mockMvc.perform(
                    put("/v1/api/blogs/{id}", postIdCreated).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(ObjectMapper().writeValueAsString(post))
                )
                    .andDo(print()).andExpect(status().isOk)
                    .andExpectAll(
                        jsonPath("$.id").value(postIdCreated),
                        jsonPath("$.title").value(post.title),
                        jsonPath("$.friendlyUrl").value(post.friendlyUrl),
                        jsonPath("$.content").value(post.content),
                    ).andReturn()
            }

            @Test
            fun `When BlogPost id does not exist then return BAD_REQUEST`() {
                val post =
                    BlogPost(title = "Title updated", friendlyUrl = "friendlyUrl updated", content = "content updated")
                mockMvc.perform(
                    put("/v1/api/blogs/{id}", "123").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(ObjectMapper().writeValueAsString(post)))
                    .andDo(print()).andExpect(status().isBadRequest)
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))

            }


        }

        @Nested
        @Order(4)
        @DisplayName("Should delete blog post by id. DELETE: /v1/api/blogs/{id}")
        inner class DeleteBlogTest {

            @Test
            @DisplayName("When BlogPost.id does exist then delete blog post by id")
            fun test_delete_blog() {
                mockMvc.perform(delete("/v1/api/blogs/{id}", postIdCreated))
                    .andDo(print()).andExpect(status().isOk)
            }

            @Test
            @DisplayName("When BlogPost.id does not exist then then return BAD_REQUEST")
            fun test_delete_blog_not_existing_id() {
                mockMvc.perform(delete("/v1/api/blogs/{id}", "123"))
                    .andDo(print()).andExpect(status().isBadRequest)
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            }
    }
}