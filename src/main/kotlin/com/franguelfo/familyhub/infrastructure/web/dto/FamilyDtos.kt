package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.family.Family
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class CreateFamilyRequest(
    @field:NotBlank(message = "El nombre de la familia es obligatorio")
    val name: String
)

data class FamilyResponse(
    val id: UUID,
    val name: String,
    val members: List<MemberResponse>
)

fun Family.toResponse(): FamilyResponse = FamilyResponse(
    id = this.id,
    name = this.name,
    members = this.members.map { it.toResponse() } // Reutiliza la extensión de Member
)