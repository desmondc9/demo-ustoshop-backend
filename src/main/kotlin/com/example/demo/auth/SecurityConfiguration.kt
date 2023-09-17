package com.example.demo.auth

import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.auth.services.MongoUserDetailsService
import com.example.demo.users.repositories.IUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfiguration(
    val userRepository: IUserRepository,
    val userAuthoritiesRepository: IUserAuthoritiesRepository,
) {
    @Bean
    fun getPasswordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun getUserDetailsService(): UserDetailsService {
        return MongoUserDetailsService(userRepository, userAuthoritiesRepository)
    }

    @Bean
    fun getSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .formLogin { it.disable() }
        .logout { it.disable() }
        .csrf { it.disable() }
        // only use http basic in the demo, can be easily changed to other methods using customized filter
        .httpBasic(Customizer.withDefaults())
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests {
            it.requestMatchers(
                "/actuator/**",
                "/login",
            ).permitAll()
                .anyRequest().authenticated()
        }
        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("127.0.0.1:5173", "localhost:5173") // or specify a list of allowed origins
        configuration.setAllowedMethods(
            listOf(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "HEAD",
                "OPTIONS"
            )
        ) // or specify a list of allowed methods
        configuration.allowedHeaders = listOf("*") // or specify a list of allowed headers
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
