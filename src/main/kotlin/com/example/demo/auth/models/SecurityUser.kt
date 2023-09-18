package com.example.demo.auth.models

import com.example.demo.users.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class SecurityUser(
    private val user: User,
    private val authorities: List<Authority>,
) : UserDetails {
    fun getUserId(): String = user.id
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        authorities.map { GrantedAuthority { it.value } }.toMutableList()

    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.name
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
