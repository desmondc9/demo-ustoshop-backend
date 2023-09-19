package com.example.demo.users.apis

import com.example.demo.users.services.CreateUserCommand
import com.example.demo.users.services.DeleteUserCommand
import com.example.demo.users.services.UserCommandHandler
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
@Validated
class UserController(
    val userCommandHandler: UserCommandHandler,
    val passwordEncoder: PasswordEncoder,
) {
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody req: CreateUserRequest) = try {
        val cmd = CreateUserCommand(
            username = req.username,
            password = passwordEncoder.encode(req.password),
            isAdmin = req.isAdmin,
        )
        val result = userCommandHandler.handle(cmd)
        result
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message, e)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteUser(@PathVariable("id") id: String) = try {
        val cmd = DeleteUserCommand(
            id = id,
        )
        val result = userCommandHandler.handle(cmd)
        result
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message, e)
    }
}


data class CreateUserRequest(
    @NotNull val username: String,
    @NotNull val password: String,
    @NotNull val isAdmin: Boolean,
)
