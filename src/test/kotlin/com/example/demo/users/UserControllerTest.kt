package com.example.demo.users

import com.example.demo.MongoDBTest
import com.example.demo.auth.models.Authority
import com.example.demo.auth.repositories.IUserAuthoritiesRepository
import com.example.demo.core.nextString
import com.example.demo.users.UserTestHelper.Companion.adminPassword
import com.example.demo.users.UserTestHelper.Companion.adminUsername
import com.example.demo.users.UserTestHelper.Companion.createUserInDatabase
import com.example.demo.users.apis.CreateUserRequest
import com.example.demo.users.repositories.IUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.random.Random

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest(
    @Autowired val userRepository: IUserRepository,
    @Autowired val userAuthoritiesRepository: IUserAuthoritiesRepository,
    @Autowired val client: TestRestTemplate,
    @Autowired val passwordEncoder: PasswordEncoder,
) : MongoDBTest() {

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        userAuthoritiesRepository.deleteAll()
    }

    @Nested
    inner class `Given users` {
        @Test
        fun `it should create user`() {
            // create user first, cause setUp will clean all user data, and client will have no authority to create user
            createUserInDatabase(
                adminUsername,
                adminPassword,
                userRepository::save,
                userAuthoritiesRepository::save,
                listOf(Authority.ADMIN)
            )

            val req = CreateUserRequest(
                username = Random.nextString(10),
                password = Random.nextString(10),
                isAdmin = Random.nextBoolean(),
            )

            val headers = HttpHeaders().also { it.setBasicAuth(adminUsername, adminPassword) }
            val httpEntity = HttpEntity(req, headers)


            val result = client.exchange("/users", HttpMethod.POST, httpEntity, Unit::class.java)

            assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)

            val actualUser = userRepository.findByName(req.username)
            val actualUserAuthorities = userAuthoritiesRepository.findByUserId(actualUser!!.id)

            assertThat(actualUser.id).isNotEmpty()
            assertThat(actualUser.name).isEqualTo(req.username)
            assertThat(actualUser.password).isNotEqualTo(req.password)
            assertThat(actualUser.version).isEqualTo(0)

            if (req.isAdmin) {
                val actualAuthorities = actualUserAuthorities.get()
                assertThat(actualAuthorities.userId).isEqualTo(actualUser.id)
                assertThat(actualAuthorities.id).isNotEmpty()
                assertThat(actualAuthorities.version).isEqualTo(0)
                assertThat(actualAuthorities.authorities).containsExactlyInAnyOrder(Authority.ADMIN)
            } else {
                assertThat(actualUserAuthorities).isEmpty()
            }

        }
    }
}
