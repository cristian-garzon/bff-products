package com.technicaltest.bffproducts.model

data class AuthenticationRequest(
    val email: String,
    val password: String,
)

data class AuthenticationResponse(
    val accessToken: String,
)