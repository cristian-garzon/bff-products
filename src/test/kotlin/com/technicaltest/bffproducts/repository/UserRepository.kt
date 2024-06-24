package com.technicaltest.bffproducts.repository

import com.technicaltest.bffproducts.model.Role
import com.technicaltest.bffproducts.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        passwordEncoder = Mockito.mock(PasswordEncoder::class.java)
        Mockito.`when`(passwordEncoder.encode("secret")).thenReturn("encoded_secret")

        userRepository = UserRepository(passwordEncoder)
    }

    @Test
    fun `test findByEmail returns user when email exists`() {
        val userEmail = "test@test.com"
        val user = userRepository.findByEmail(userEmail)

        assertNotNull(user)
        assertEquals(userEmail, user?.email)
        assertEquals("encoded_secret", user?.password)
        assertEquals(Role.ADMINISTRATOR, user?.role)
    }

    @Test
    fun `test findByEmail returns null when email does not exist`() {
        val userEmail = "nonexistent@test.com"
        val user = userRepository.findByEmail(userEmail)

        assertNull(user)
    }

}
