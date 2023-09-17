package com.example.demo.users

import com.example.demo.auth.models.Authority
import com.example.demo.users.models.User
import org.bson.types.ObjectId
import org.springframework.security.crypto.factory.PasswordEncoderFactories

class UserTestHelper {
    companion object {
        fun createUserInDatabase(
            username: String,
            password: String,
            saveToDatabaseFun: (User) -> Unit,
            authorities: List<Authority> = emptyList(),
        ) {
            val user = User(
                id = ObjectId().toString(),
                name = username,
                version = null,
                password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password)
            )
            saveToDatabaseFun(user)
        }
    }
}
