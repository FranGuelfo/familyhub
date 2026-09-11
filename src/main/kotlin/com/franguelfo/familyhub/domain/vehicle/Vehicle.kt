package com.franguelfo.familyhub.domain.vehicle

import java.util.UUID

data class Vehicle(
    val id: UUID = UUID.randomUUID(),
    val familyId: UUID,
    val brand: String,
    val model: String,
    val licensePlate: LicensePlate,
    val status: VehicleStatus = VehicleStatus.AVAILABLE,
    val assignedMemberId: UUID? = null
) {
    fun assignTo(memberId: UUID): Vehicle =
        this.copy(assignedMemberId = memberId, status = VehicleStatus.IN_USE)

    fun release(): Vehicle =
        this.copy(assignedMemberId = null, status = VehicleStatus.AVAILABLE)

    fun markInMaintenance(): Vehicle =
        this.copy(status = VehicleStatus.MAINTENANCE)
}