package com.franguelfo.familyhub.domain.garage

import java.util.UUID

data class ParkingSpot(
    val id: UUID = UUID.randomUUID(),
    val familyId: UUID,
    val spotNumber: String,
    val hasEvCharger: Boolean = false,
    val status: SpotStatus = SpotStatus.Available
) {
    fun park(vehicleId: UUID): ParkingSpot {
        if (status !is SpotStatus.Available) {
            throw IllegalStateException("La plaza '$spotNumber' no está disponible para aparcar.")
        }
        return this.copy(status = SpotStatus.Occupied(vehicleId))
    }

    fun vacate(): ParkingSpot {
        if (status !is SpotStatus.Occupied) {
            throw IllegalStateException("La plaza '$spotNumber' ya se encuentra desocupada.")
        }
        return this.copy(status = SpotStatus.Available)
    }

    fun markOutOfService(reason: String): ParkingSpot =
        this.copy(status = SpotStatus.OutOfService(reason))
}