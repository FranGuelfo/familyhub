package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.infrastructure.security.AuthService
import com.franguelfo.familyhub.infrastructure.web.dto.AuthResponse
import com.franguelfo.familyhub.infrastructure.web.dto.LoginRequest
import com.franguelfo.familyhub.infrastructure.web.dto.RegisterRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints públicos de registro y login")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo miembro", description = "Crea un usuario y genera un JWT con el rol especificado")
    @SecurityRequirements
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve el token JWT")
    @SecurityRequirements
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}