package com.blog.configuration

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.lang.Exception

@Component
class LogInterceptor : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(LogInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestWrapper = ContentCachingRequestWrapper(request)
        logger.info("_Rest_| [REQUEST]: [Method: {}] [URI: {}] [Headers: {}] [Body: {}]",
            requestWrapper.method,
            requestWrapper.requestURI,
            getHeaders(requestWrapper),
            requestWrapper.contentAsString)
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        val wrapper = ContentCachingResponseWrapper(response)
        logger.info("_Rest_| [RESPONSE]:  [Status: {}] [Headers: {}] [Body: {}]",
            wrapper.status,
            wrapper.headerNames.associateWith { wrapper.getHeader(it) },
            String(wrapper.contentAsByteArray))
        wrapper.copyBodyToResponse()
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        return request.headerNames.asSequence().associateWith { request.getHeader(it) }
    }

}