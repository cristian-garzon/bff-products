package com.technicaltest.bffproducts.repository

import com.technicaltest.bffproducts.model.Role
import com.technicaltest.bffproducts.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(
    private val encoder: PasswordEncoder
) {

    private val users = mutableListOf<User>(
        User(
            id = UUID.randomUUID(),
            email = "test@test.com",
            password = encoder.encode("secret"),
            Role.ADMINISTRATOR
        ),
    )

    fun findByEmail(email: String): User? = users.firstOrNull { it.email == email }

}