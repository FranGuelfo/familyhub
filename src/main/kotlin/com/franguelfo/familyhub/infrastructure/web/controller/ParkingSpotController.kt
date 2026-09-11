package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.garage.ParkingSpotService
import com.franguelfo.familyhub.infrastructure.web.dto.CreateParkingSpotRequest
import com.franguelfo.familyhub.infrastructure.web.dto.ParkingSpotResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/parking-spots")
@Tag(name = "Parking", description = "Endpoints de gestión de parking")
class ParkingSpotController(
    private val parkingSpotService: ParkingSpotService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear plaza de parking", description = "Crea una plaza de parking para una familia")
    fun createSpot(@Valid @RequestBody request: CreateParkingSpotRequest): ParkingSpotResponse {
        val spot = parkingSpotService.createSpot(
            familyId = request.familyId,
            spotNumber = request.spotNumber,
            hasEvCharger = request.hasEvCharger
        )
        return spot.toResponse()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plaza de parking por ID", description = "Devuelve el detalle de una plaza de parking a partir de su identificador")
    fun getSpotById(@PathVariable id: UUID): ResponseEntity<ParkingSpotResponse> =
        ResponseEntity.ok(parkingSpotService.getSpotById(id).toResponse())

    @GetMapping
    @Operation(summary = "Listar plazas de parking", description = "Devuelve todas las plazas de parking o las filtra por familia")
    fun getSpots(@RequestParam(required = false) familyId: UUID?): List<ParkingSpotResponse> {
        val spots = if (familyId != null) {
            parkingSpotService.getSpotsByFamily(familyId)
        } else {
            parkingSpotService.getAllSpots()
        }
        return spots.map { it.toResponse() }
    }

    @PatchMapping("/{id}/park/{vehicleId}")
    @Operation(summary = "Aparcar vehículo", description = "Asigna un vehículo a una plaza de parking")
    fun parkVehicle(
        @PathVariable id: UUID,
        @PathVariable vehicleId: UUID
    ): ResponseEntity<ParkingSpotResponse> {
        val updated = parkingSpotService.parkVehicle(id, vehicleId)
        return ResponseEntity.ok(updated.toResponse())
    }

    @PatchMapping("/{id}/vacate")
    @Operation(summary = "Liberar plaza de parking", description = "Quita el vehículo asignado a una plaza de parking")
    fun vacateSpot(@PathVariable id: UUID): ResponseEntity<ParkingSpotResponse> {
        val updated = parkingSpotService.vacateSpot(id)
        return ResponseEntity.ok(updated.toResponse())
    }
}
