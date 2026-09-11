package com.franguelfo.familyhub.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret-key}") private val secretKeyString: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long
) {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateToken(
        memberId: UUID,
        email: String,
        role: String,
        extraClaims: Map<String, Any> = emptyMap()
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(email)
            .claims(extraClaims)
            .claim("memberId", memberId.toString())
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(signingKey)
            .compact()
    }

    fun extractEmail(token: String): String = extractAllClaims(token).subject

    fun extractMemberId(token: String): UUID =
        UUID.fromString(extractAllClaims(token)["memberId"] as String)

    fun extractRole(token: String): String =
        extractAllClaims(token)["role"] as String

    fun isTokenValid(token: String): Boolean =
        runCatching {
            val claims = extractAllClaims(token)
            !claims.expiration.before(Date())
        }.getOrDefault(false)

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
}