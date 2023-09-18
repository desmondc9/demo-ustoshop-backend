package com.example.demo.auth.apis

import com.example.demo.auth.models.SecurityUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
@Validated
class AuthController {
    @PostMapping("/login")
    fun login() {
        return Unit
    }

    @GetMapping("/authorities")
    fun getAuthorities(@AuthenticationPrincipal userDetails: SecurityUser): List<String> {
        return userDetails.authorities.map { it.authority.toString() }
    }
}
