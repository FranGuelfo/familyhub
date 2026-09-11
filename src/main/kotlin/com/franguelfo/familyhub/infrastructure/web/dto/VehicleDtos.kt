package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RegisterVehicleRequest(
    @field:NotNull(message = "El ID de la familia es obligatorio")
    val familyId: UUID,

    @field:NotBlank(message = "La marca es obligatoria")
    val brand: String,

    @field:NotBlank(message = "El modelo es obligatorio")
    val model: String,

    @field:NotBlank(message = "La matrícula es obligatoria")
    val licensePlate: String
)

data class VehicleResponse(
    val id: UUID,
    val familyId: UUID,
    val brand: String,
    val model: String,
    val licensePlate: String,
    val status: VehicleStatus,
    val assignedMemberId: UUID?
)

// Desempaquetamos el Value Class con .value para exponer un String limpio en la API
fun Vehicle.toResponse(): VehicleResponse = VehicleResponse(
    id = this.id,
    familyId = this.familyId,
    brand = this.brand,
    model = this.model,
    licensePlate = this.licensePlate.value,
    status = this.status,
    assignedMemberId = this.assignedMemberId
)