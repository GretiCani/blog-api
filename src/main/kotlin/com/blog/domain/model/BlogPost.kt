package com.blog.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BlogPost(
    var id: String? = null,
    @get: NotBlank(message = "Title cannot be blank")
    var title: String,
    @get: Size(max = 1000, message = "Content length must be mx 1000 characters")
    var content: String,
    @get:NotBlank(message = "friendlyUrl cannot be blank")
    var friendlyUrl: String
) {
    @JsonCreator
    constructor() : this("", "", "", "")
}