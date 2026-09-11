package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.member.MemberService
import com.franguelfo.familyhub.infrastructure.web.dto.CreateMemberRequest
import com.franguelfo.familyhub.infrastructure.web.dto.MemberResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/members")
@Tag(name = "Miembros", description = "Endpoints de gestión de los miembros")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear miembro", description = "Crea un nuevo miembro con nombre, email y rol")
    fun createMember(@Valid @RequestBody request: CreateMemberRequest): MemberResponse {
        val created = memberService.createMember(request.name, request.email, request.role)
        return created.toResponse()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener miembro por ID", description = "Devuelve el detalle de un miembro a partir de su identificador")
    fun getMemberById(@PathVariable id: UUID): ResponseEntity<MemberResponse> {
        val member = memberService.getMemberById(id)
        return ResponseEntity.ok(member.toResponse())
    }

    @GetMapping
    @Operation(summary = "Listar miembros", description = "Devuelve todos los miembros registrados")
    fun getAllMembers(): List<MemberResponse> =
        memberService.getAllMembers().map { it.toResponse() }
}
