package com.franguelfo.familyhub.infrastructure.security

import com.franguelfo.familyhub.application.family.FamilyService
import com.franguelfo.familyhub.application.vehicle.VehicleService
import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.infrastructure.security.jwt.JwtAuthenticationFilter
import com.franguelfo.familyhub.infrastructure.security.jwt.JwtService
import com.franguelfo.familyhub.infrastructure.web.controller.FamilyController
import com.franguelfo.familyhub.infrastructure.web.controller.VehicleController
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [FamilyController::class, VehicleController::class])
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class,
    DelegatingAuthenticationEntryPoint::class,
    DelegatingAccessDeniedHandler::class
)
class SecurityAccessControlTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockitoBean
    private lateinit var familyService: FamilyService

    @MockitoBean
    private lateinit var vehicleService: VehicleService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @BeforeEach
    fun clearSecurityContextBeforeTest() {
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun clearSecurityContextAfterTest() {
        SecurityContextHolder.clearContext()
    }

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
            with(user("hijo@familyhub.com").roles("MEMBER"))
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
    fun `debe permitir el acceso a un ADMIN para listar familias`() {
        given(familyService.getAllFamilies()).willReturn(listOf(Family(name = "Familia Test")))

        // Usamos /api/families que sí está implementado con GET
        mockMvc.get("/api/families") {
            with(user("fran@familyhub.com").roles("ADMIN"))
        }
            .andExpect {
                status { isOk() }
            }
    }
}
