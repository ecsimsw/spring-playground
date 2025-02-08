package com.ecsimsw.business

import com.ecsimsw.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/hello")
    fun sayHello(name: String = "World"): ApiResponse<String> {
        return ApiResponse.success("Hello, $name!")
    }
}