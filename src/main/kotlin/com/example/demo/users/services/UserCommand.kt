package com.example.demo.users.services

sealed interface UserCommand

data class CreateUserCommand(
    val username: String,
    val password: String,
    val isAdmin: Boolean,
) : UserCommand

data class DeleteUserCommand(
    val id: String,
) : UserCommand
