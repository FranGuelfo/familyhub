package com.franguelfo.familyhub.infrastructure.web.dto

import java.util.UUID

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "PARENT"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val memberId: UUID,
    val email: String,
    val role: String
)