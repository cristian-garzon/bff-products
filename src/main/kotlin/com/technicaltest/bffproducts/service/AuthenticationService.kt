package com.technicaltest.bffproducts.service

import com.technicaltest.bffproducts.configuration.JwtProperties
import com.technicaltest.bffproducts.model.AuthenticationRequest
import com.technicaltest.bffproducts.model.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthenticationService (
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {

    fun authenticate(authentication: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authentication.email,
                authentication.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authentication.email)
        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )
        return AuthenticationResponse(
            accessToken = accessToken
        )
    }

}