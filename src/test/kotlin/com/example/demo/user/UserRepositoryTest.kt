package com.shisanfan.shisanfanauthspring.user

import com.shisanfan.shisanfanauthspring.user.domain.User
import com.shisanfan.shisanfanauthspring.user.infrastructure.IUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest(
    @Autowired val repository: IUserRepository,
) {

    @Test
    fun `should save user into db`() {
        val u1 = User(
            id = ObjectId().toHexString(),
            name = "test",
            password = "test",
        )
        repository.save(u1)

        val actual = repository.findById(u1.id)

        assertThat(actual).isPresent
    }
}
