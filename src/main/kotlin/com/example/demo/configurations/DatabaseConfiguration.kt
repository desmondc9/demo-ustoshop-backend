package com.example.demo.configurations

import com.example.demo.auth.models.Authority
import com.example.demo.auth.models.UserAuthorities
import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.users.models.User
import com.example.demo.users.repositories.IUserRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.security.crypto.password.PasswordEncoder

@EnableMongoAuditing
@Configuration
class DatabaseConfiguration(
    val passwordEncoder: PasswordEncoder,
) {

    val logger: Logger = LoggerFactory.getLogger(DatabaseConfiguration::class.java)

    // For Demo only, automatically create an admin user if it doesn't exist in the database when app starts
    @Bean
    fun initDatabase(
        userRepository: IUserRepository,
        userAuthoritiesRepository: IUserAuthoritiesRepository,
    ): CommandLineRunner = CommandLineRunner {
        userRepository.findByName("admin")?.let {
            logger.info("Admin user already exists")
        } ?: run {
            val user = User(
                id = ObjectId().toString(),
                version = null,
                name = "admin",
                password = passwordEncoder.encode("pa\$sword!")
            )
            userRepository.save(user)
            userAuthoritiesRepository.save(
                UserAuthorities(
                    id = ObjectId().toString(),
                    version = null,
                    userId = user.id,
                    authorities = listOf(Authority.ADMIN)
                )
            )
        }

    }
}

