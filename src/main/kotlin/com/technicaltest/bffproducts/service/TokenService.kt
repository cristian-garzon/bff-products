package com.technicaltest.bffproducts.service

import com.technicaltest.bffproducts.configuration.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    val jwtProperties: JwtProperties
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )


    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String = Jwts.builder()
        .claims()
        .subject(userDetails.username)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(expirationDate)
        .add(additionalClaims)
        .and()
        .signWith(secretKey)
        .compact()


    fun extractEmail(token: String): String? = getAllClaims(token).subject

    fun isExpired(token: String): Boolean = getAllClaims(token).expiration.before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser.parseSignedClaims(token.trim())
            .payload
    }

    fun isValid(token: String, UserDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return UserDetails.username == email && !isExpired(token)
    }
}