package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.vehicle.VehicleService
import com.franguelfo.familyhub.infrastructure.web.dto.RegisterVehicleRequest
import com.franguelfo.familyhub.infrastructure.web.dto.VehicleResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehículos", description = "Endpoints de gestión de vehículos")
class VehicleController(
    private val vehicleService: VehicleService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar vehículo", description = "Registra un vehículo para una familia")
    fun registerVehicle(@Valid @RequestBody request: RegisterVehicleRequest): VehicleResponse {
        val vehicle = vehicleService.registerVehicle(
            familyId = request.familyId,
            brand = request.brand,
            model = request.model,
            licensePlateText = request.licensePlate
        )
        return vehicle.toResponse()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehículo por ID", description = "Devuelve el detalle de un vehículo a partir de su identificador")
    fun getVehicleById(@PathVariable id: UUID): ResponseEntity<VehicleResponse> =
        ResponseEntity.ok(vehicleService.getVehicleById(id).toResponse())

    @GetMapping
    @Operation(summary = "Listar vehículos", description = "Devuelve todos los vehículos o los filtra por familia")
    fun getVehicles(@RequestParam(required = false) familyId: UUID?): List<VehicleResponse> {
        val vehicles = if (familyId != null) {
            vehicleService.getVehiclesByFamily(familyId)
        } else {
            vehicleService.getAllVehicles()
        }
        return vehicles.map { it.toResponse() }
    }

    @PatchMapping("/{id}/assign/{memberId}")
    @Operation(summary = "Asignar vehículo", description = "Asigna un vehículo a un miembro")
    fun assignVehicle(
        @PathVariable id: UUID,
        @PathVariable memberId: UUID
    ): ResponseEntity<VehicleResponse> {
        val updated = vehicleService.assignVehicle(id, memberId)
        return ResponseEntity.ok(updated.toResponse())
    }

    @PatchMapping("/{id}/release")
    @Operation(summary = "Liberar vehículo", description = "Quita la asignación actual de un vehículo")
    fun releaseVehicle(@PathVariable id: UUID): ResponseEntity<VehicleResponse> {
        val updated = vehicleService.releaseVehicle(id)
        return ResponseEntity.ok(updated.toResponse())
    }
}
