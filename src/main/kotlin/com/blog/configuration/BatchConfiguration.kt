package com.blog.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
import javax.sql.DataSource

@Configuration
class BatchConfiguration {

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .addScript("/org/springframework/batch/core/schema-drop-h2.sql")
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.H2)
            .build()
    }

    @Bean
    fun transactionManager():JdbcTransactionManager{
        return JdbcTransactionManager(dataSource())
    }
}