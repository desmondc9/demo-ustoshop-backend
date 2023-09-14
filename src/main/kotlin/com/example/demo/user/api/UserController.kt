package com.example.demo.user.api

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun getUsers(): String {
        return "hello"
    }

    @GetMapping("/file", produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun downloadFile(): ByteArray {
        // load a file from disk and return it to frontend as a binary file
        val file = java.io.File("src/main/resources/static/WechatIMG169.jpg")
        return file.readBytes()
    }
}
