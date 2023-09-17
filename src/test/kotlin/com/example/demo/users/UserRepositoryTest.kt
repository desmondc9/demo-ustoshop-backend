package com.example.demo.users

import com.example.demo.MongoDBTest
import com.example.demo.users.models.User
import com.example.demo.users.repositories.IUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest(
    @Autowired val repository: IUserRepository,
) : MongoDBTest() {
    @Test
    fun `should save user into db`() {
        val u1 = User(
            id = ObjectId().toString(),
            name = "test",
            password = "test",
        )
        repository.save(u1)

        val actual = repository.findById(u1.id)

        assertThat(actual).isPresent
    }
}
