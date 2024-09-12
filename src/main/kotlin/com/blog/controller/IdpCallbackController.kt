package com.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/idp/callback")
class IdpCallbackController {

    @GetMapping
    fun getIdpCallback(@RequestParam(name = "access_token") accessToken: String) : Map<String, String> {
        return mapOf("accessToken" to "Bearer $accessToken")
    }
}