package com.franguelfo.familyhub.infrastructure.security

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAccessControlTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `debe devolver 401 Unauthorized al consultar familias sin token`() {
        mockMvc.get("/api/families")
            .andExpect {
                status { isUnauthorized() }
                jsonPath("$.status") { value(401) }
                jsonPath("$.error") { value("Unauthorized") }
                jsonPath("$.message") { value("Autenticación requerida para acceder a este recurso") }
            }
    }

    @Test
    @WithMockUser(username = "hijo@familyhub.com", roles = ["MEMBER"])
    fun `debe devolver 403 Forbidden cuando un MEMBER intenta registrar un vehiculo`() {
        val payload = """
            {
                "familyId": "a0000000-0000-0000-0000-000000000001",
                "brand": "Renault",
                "model": "Clio",
                "licensePlate": "0000XYZ"
            }
        """.trimIndent()

        mockMvc.post("/api/vehicles") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isForbidden() }
            jsonPath("$.status") { value(403) }
            jsonPath("$.error") { value("Forbidden") }
            jsonPath("$.message") { value("No tienes permisos suficientes para realizar esta acción") }
        }
    }

    @Test
    @WithMockUser(username = "fran@familyhub.com", roles = ["ADMIN"])
    fun `debe permitir el acceso a un ADMIN para listar vehiculos`() {
        mockMvc.get("/api/vehicles")
            .andExpect {
                status { isOk() }
            }
    }
}