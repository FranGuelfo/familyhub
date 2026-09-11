package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.SpotStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateParkingSpotRequest(
    @field:NotNull(message = "El ID de la familia es obligatorio")
    val familyId: UUID,

    @field:NotBlank(message = "El número o identificador de plaza es obligatorio")
    val spotNumber: String,

    val hasEvCharger: Boolean = false
)

data class ParkingSpotResponse(
    val id: UUID,
    val familyId: UUID,
    val spotNumber: String,
    val hasEvCharger: Boolean,
    val status: String,
    val parkedVehicleId: UUID?,
    val outOfServiceReason: String?
)

fun ParkingSpot.toResponse(): ParkingSpotResponse = when (val currentStatus = this.status) {
    is SpotStatus.Available -> ParkingSpotResponse(
        id = this.id,
        familyId = this.familyId,
        spotNumber = this.spotNumber,
        hasEvCharger = this.hasEvCharger,
        status = "AVAILABLE",
        parkedVehicleId = null,
        outOfServiceReason = null
    )
    is SpotStatus.Occupied -> ParkingSpotResponse(
        id = this.id,
        familyId = this.familyId,
        spotNumber = this.spotNumber,
        hasEvCharger = this.hasEvCharger,
        status = "OCCUPIED",
        parkedVehicleId = currentStatus.vehicleId, // Smart cast automático a Occupied
        outOfServiceReason = null
    )
    is SpotStatus.OutOfService -> ParkingSpotResponse(
        id = this.id,
        familyId = this.familyId,
        spotNumber = this.spotNumber,
        hasEvCharger = this.hasEvCharger,
        status = "OUT_OF_SERVICE",
        parkedVehicleId = null,
        outOfServiceReason = currentStatus.reason // Smart cast automático a OutOfService
    )
}