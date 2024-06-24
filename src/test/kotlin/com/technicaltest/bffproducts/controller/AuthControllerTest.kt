package com.technicaltest.bffproducts.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`

import com.technicaltest.bffproducts.controller.AuthController
import com.technicaltest.bffproducts.model.AuthenticationRequest
import com.technicaltest.bffproducts.model.AuthenticationResponse
import com.technicaltest.bffproducts.service.AuthenticationService

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class AuthControllerTest {
    @Mock
    private val authenticationService: AuthenticationService? = null

    private var authController: AuthController? = null

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        authController = AuthController(authenticationService!!)
    }

    @Test
    fun testAuthenticateSuccess() {
        val mockRequest = AuthenticationRequest("username", "password")
        val mockResponse = AuthenticationResponse("token")

        `when`(authenticationService!!.authenticate(mockRequest)).thenReturn(mockResponse)

        // Call the method under test
        val response = authController!!.authenticate(mockRequest)

        // Assert the results
        assertEquals(mockResponse, response)
    }

    @Test
    fun testAuthenticateFailure() {
        // Create mock objects
        val mockRequest = AuthenticationRequest("invalid", "credentials")
        val mockResponse: AuthenticationResponse? = null // Simulate authentication failure

        // Set up mock behavior
        `when`(authenticationService!!.authenticate(mockRequest)).thenReturn(mockResponse)

        // Call the method under test
        val response = authController!!.authenticate(mockRequest)

        // Assert the results (handle null response or specific error object)
        assertEquals(null, response) // Or assert on specific error message/object
    }
}