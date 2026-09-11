package com.franguelfo.familyhub.infrastructure.persistence.mapper

import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.SpotStatus
import com.franguelfo.familyhub.infrastructure.persistence.entity.ParkingSpotJpaEntity

fun ParkingSpot.toEntity(): ParkingSpotJpaEntity {
    val (statusType, vehicleId, reason) = when (val s = this.status) {
        is SpotStatus.Available -> Triple("AVAILABLE", null, null)
        is SpotStatus.Occupied -> Triple("OCCUPIED", s.vehicleId, null)
        is SpotStatus.OutOfService -> Triple("OUT_OF_SERVICE", null, s.reason)
    }

    return ParkingSpotJpaEntity(
        id = this.id,
        familyId = this.familyId,
        spotNumber = this.spotNumber,
        hasEvCharger = this.hasEvCharger,
        statusType = statusType,
        parkedVehicleId = vehicleId,
        outOfServiceReason = reason
    )
}

fun ParkingSpotJpaEntity.toDomain(): ParkingSpot {
    val domainStatus = when (this.statusType) {
        "AVAILABLE" -> SpotStatus.Available
        "OCCUPIED" -> SpotStatus.Occupied(
            vehicleId = checkNotNull(this.parkedVehicleId) {
                "parkedVehicleId no puede ser nulo para estado OCCUPIED"
            }
        )
        "OUT_OF_SERVICE" -> SpotStatus.OutOfService(
            reason = this.outOfServiceReason ?: ""
        )
        else -> error("Tipo de estado desconocido: ${this.statusType}")
    }

    return ParkingSpot(
        id = this.id,
        familyId = this.familyId,
        spotNumber = this.spotNumber,
        hasEvCharger = this.hasEvCharger,
        status = domainStatus
    )
}