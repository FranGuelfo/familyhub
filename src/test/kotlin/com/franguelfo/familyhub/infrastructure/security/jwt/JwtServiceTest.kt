package com.franguelfo.familyhub.infrastructure.security.jwt

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class JwtServiceTest {

    private val secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
    private val expirationMs = 3600000L // 1 hora
    private val jwtService = JwtService(secret, expirationMs)

    @Test
    fun `debe generar un token valido y extraer sus claims correctamente`() {
        val memberId = UUID.randomUUID()
        val email = "fran@familyhub.com"
        val role = "ADMIN"

        val token = jwtService.generateToken(memberId, email, role)

        Assertions.assertThat(token).isNotBlank()
        Assertions.assertThat(jwtService.isTokenValid(token)).isTrue()
        Assertions.assertThat(jwtService.extractEmail(token)).isEqualTo(email)
        Assertions.assertThat(jwtService.extractMemberId(token)).isEqualTo(memberId)
        Assertions.assertThat(jwtService.extractRole(token)).isEqualTo(role)
    }

    @Test
    fun `debe rechazar un token expirado o manipulado`() {
        val expiredService = JwtService(secret, -1000L) // Expirado hace un segundo
        val token = expiredService.generateToken(UUID.randomUUID(), "caducado@familyhub.com", "MEMBER")

        Assertions.assertThat(jwtService.isTokenValid(token)).isFalse()
    }
}