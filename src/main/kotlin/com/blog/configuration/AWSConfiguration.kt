package com.blog.configuration

import org.springframework.beans.factory.annotation.Value
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

abstract class AWSConfiguration {

    @Value("\${aws.region}")
    lateinit var region: String

    @Value("\${aws.services.dynamodb.key}")
    lateinit var dynamoDbKey: String

    @Value("\${aws.services.dynamodb.secret}")
    lateinit var dynamoDbSecret: String

    protected fun awsCredentialProvider(): AwsCredentialsProvider{
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(dynamoDbKey, dynamoDbSecret))
    }
}