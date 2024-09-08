package com.blog.batch

import com.blog.configuration.BatchConfiguration
import com.blog.domain.entity.Post
import com.blog.domain.model.BlogPost
import com.blog.repository.BlogRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.jdbc.support.JdbcTransactionManager

@Configuration
@EnableBatchProcessing
@Import(BatchConfiguration::class)
class BlogUploadJobConfiguration {

    @Bean
    @StepScope
    fun blogPostItemReader(@Value("#{jobParameters['inputFile']}") inputFile: String): FlatFileItemReader<BlogPost> {
        val resource: Resource = UrlResource(inputFile)
        val reader= FlatFileItemReaderBuilder<BlogPost>()
            .name("blogPostItemReader")
            .resource(resource)
            .delimited()
            .names("id", "title","friendlyUrl","content")
            .targetType(BlogPost::class.java)
            .build()
        reader.setLinesToSkip(1)
        return reader
    }

    @Bean
    @StepScope
    fun blogPostItemWriter(@Autowired blogRepository:BlogRepository): DynamoDbItemWriter<Post,String>{
        val blogPostItemWriter: DynamoDbItemWriter<Post,String> = DynamoDbItemWriter()
        blogPostItemWriter.setRepository(blogRepository)
        return blogPostItemWriter
    }

    @Bean
    fun step(jobRepository: JobRepository, transactionManager: JdbcTransactionManager,
             blogPostItemReader: ItemReader<BlogPost>,
             blogPostProcessor: ItemProcessor<BlogPost, Post>,
             blogPostItemWriter: ItemWriter<Post>
             ): Step{
        return StepBuilder("Step 1",jobRepository).chunk<BlogPost, Post>(10,transactionManager)
            .reader(blogPostItemReader).processor(blogPostProcessor).writer(blogPostItemWriter).build()
    }

    @Bean
    fun blogPostProcessor(): BlogPostProcessor{
        return BlogPostProcessor()
    }

    @Bean
    fun blogJob(jobRepository: JobRepository, step: Step) : Job{
        return JobBuilder("job-1",jobRepository).start(step).build()
    }
}