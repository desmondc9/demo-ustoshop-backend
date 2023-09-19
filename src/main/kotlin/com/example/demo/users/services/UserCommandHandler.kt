package com.example.demo.users.services

import com.example.demo.auth.models.Authority
import com.example.demo.auth.models.UserAuthorities
import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.users.models.User
import com.example.demo.users.repositories.IUserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class UserCommandHandler(
    val userRepository: IUserRepository,
    val userAuthoritiesRepository: IUserAuthoritiesRepository,
) {
    fun handle(cmd: CreateUserCommand) {
        val user = User(
            id = ObjectId().toString(),
            version = null,
            name = cmd.username,
            password = cmd.password,
        )
        userRepository.save(user)
        userAuthoritiesRepository.save(
            UserAuthorities(
                id = ObjectId().toString(),
                version = null,
                userId = user.id,
                authorities = if (cmd.isAdmin) listOf(Authority.ADMIN) else emptyList()
            )
        )

    }

    fun handle(cmd: DeleteUserCommand) {
        val user = userRepository.findById(cmd.id).ifPresent { user ->
            user.name.takeIf { it != "admin" }?.let { // cannot delete root admin
                userRepository.deleteById(user.id)
                userAuthoritiesRepository.findByUserId(cmd.id).map {
                    userAuthoritiesRepository.delete(it)
                }
            }
        }
    }

}
