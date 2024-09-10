package com.blog.configuration

import com.blog.domain.entity.Post
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class DynamoDbConfiguration : AWSConfiguration() {


    @Value("\${aws.services.dynamodb.uri}")
    lateinit var dynamoDbUri: String

    @Value("\${aws.services.dynamodb.blogTable}")
    lateinit var tableName: String

    @Bean
    fun dynamoDbClient(): DynamoDbEnhancedClient{
        val uri =URI.create(dynamoDbUri)
        val dynamoDbClient = DynamoDbClient.builder()
            .region(Region.of(region))
            .credentialsProvider(awsCredentialProvider())
            .endpointOverride(uri)
            .build()
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient).build()
    }

    @Bean
    fun blogPostsTable(): DynamoDbTable<Post>{
        print("table name ${tableName}")
        return dynamoDbClient().table(tableName,TableSchema.fromBean(Post::class.java))
    }

}