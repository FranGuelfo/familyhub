package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class CreateMemberRequest(
    @field:NotBlank(message = "El nombre es obligatorio")
    val name: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El formato del email no es válido")
    val email: String,

    val role: Role = Role.MEMBER
)

data class MemberResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val role: String
)

// Función de extensión: mapea de dominio a DTO
fun Member.toResponse(): MemberResponse = MemberResponse(
    id = this.id,
    name = this.name,
    email = this.email,
    role = this.role.name
)