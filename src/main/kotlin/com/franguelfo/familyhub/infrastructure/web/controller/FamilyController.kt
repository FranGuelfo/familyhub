package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.family.FamilyService
import com.franguelfo.familyhub.infrastructure.web.dto.CreateFamilyRequest
import com.franguelfo.familyhub.infrastructure.web.dto.FamilyResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/families")
@Tag(name = "Familias", description = "Endpoints para gestionar las familias")
class FamilyController(
    private val familyService: FamilyService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT')")
    @Operation(summary = "Crear familia", description = "Crea una nueva familia con el nombre indicado")
    fun createFamily(@Valid @RequestBody request: CreateFamilyRequest): FamilyResponse =
        familyService.createFamily(request.name).toResponse()

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener familia por ID", description = "Devuelve el detalle de una familia a partir de su identificador")
    fun getFamilyById(@PathVariable id: UUID): ResponseEntity<FamilyResponse> =
        ResponseEntity.ok(familyService.getFamilyById(id).toResponse())

    @PostMapping("/{familyId}/members/{memberId}")
    @Operation(summary = "Añadir miembro a una familia", description = "Asocia un miembro existente a una familia")
    fun addMemberToFamily(
        @PathVariable familyId: UUID,
        @PathVariable memberId: UUID
    ): ResponseEntity<FamilyResponse> {
        val updatedFamily = familyService.addMemberToFamily(familyId, memberId)
        return ResponseEntity.ok(updatedFamily.toResponse())
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar familias", description = "Devuelve todas las familias registradas")
    fun getAllFamilies(): List<FamilyResponse> =
        familyService.getAllFamilies().map { it.toResponse() }
}
