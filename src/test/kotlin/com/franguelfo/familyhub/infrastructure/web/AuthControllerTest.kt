package com.franguelfo.familyhub.infrastructure.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `debe registrar un usuario correctamente y devolver un token JWT`() {
        val uniqueEmail = "user_${UUID.randomUUID()}@familyhub.com"
        val payload = """
            {
                "name": "Usuario Test",
                "email": "$uniqueEmail",
                "password": "Password123!",
                "role": "MEMBER"
            }
        """.trimIndent()

        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
            jsonPath("$.token") { isNotEmpty() }
            jsonPath("$.email") { value(uniqueEmail) }
            jsonPath("$.role") { value("MEMBER") }
        }
    }

    @Test
    fun `debe devolver 401 Unauthorized con JSON de error al fallar credenciales en login`() {
        val payload = """
            {
                "email": "no_existe@familyhub.com",
                "password": "clave_incorrecta"
            }
        """.trimIndent()

        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") { value(401) }
            jsonPath("$.error") { value("Unauthorized") }
            jsonPath("$.message") { value("Credenciales inválidas") }
        }
    }
}