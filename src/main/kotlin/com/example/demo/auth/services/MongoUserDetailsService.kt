package com.example.demo.auth.services

import com.example.demo.auth.models.Authority
import com.example.demo.auth.models.SecurityUser
import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.users.models.User
import com.example.demo.users.repositories.IUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MongoUserDetailsService(
    private val userRepository: IUserRepository,
    private val userAuthoritiesRepository: IUserAuthoritiesRepository,
) : UserDetailsService {
    // First get by username, then get by id
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByName(username)?.let {
            SecurityUser(it, getUserAuthoritiesForUserId(it))
        } ?: userRepository.findById(username).map {
            SecurityUser(it, getUserAuthoritiesForUserId(it))
        }.orElseThrow { UsernameNotFoundException("User not found: $username") }
    }

    private fun getUserAuthoritiesForUserId(it: User): List<Authority> =
        userAuthoritiesRepository.findByUserId(it.id).map { it.authorities }
            .orElse(emptyList())
}
