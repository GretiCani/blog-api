package com.blog.domain.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import java.time.LocalDateTime
import java.util.*

@DynamoDbBean
class Post(

    @get:DynamoDbPartitionKey
    var id: String? = UUID.randomUUID().toString(),

    @get: NotBlank(message = "Title cannot be blank")
    @get:DynamoDbAttribute("title")
    var title: String="",

    @get:NotBlank(message = "friendlyUrl cannot be blank")
    @get:DynamoDbAttribute("friendlyUrl")
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["friendlyUrl-index"])
    var friendlyUrl: String="",

    @get: Size(max = 1000, message = "Content length must be mx 1000 characters")
    @get:DynamoDbAttribute("content")
    var content: String="",

    @get:DynamoDbAttribute( "dateCreated")
    var dateCreated: LocalDateTime = LocalDateTime.now(),

    @get:DynamoDbAttribute( "createdBy")
    var createdBy: String=""
){
    constructor() : this(id=null) {
    }

}
