package com.blog.service.impl

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class BatchService(
    @Autowired private val jobLauncher: JobLauncher,
    @Autowired private val blogJob: Job
) {
    @Value("\${aws.services.s3.upload.baseLocation}")
    lateinit var baseLocation: String

    fun launchBlogUpload(filename: String){

        val jobParam = JobParametersBuilder().addString("inputFile","$baseLocation/$filename")
            .addDate("runDate", Date())
            .toJobParameters()
        jobLauncher.run(blogJob,jobParam )

    }
}