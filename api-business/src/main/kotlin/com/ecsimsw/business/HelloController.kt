package com.ecsimsw.business

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun sayHello(@Authen name: String = "World"): Map<String, String> {
        return mapOf("message" to "Hello, $name!")
    }
}