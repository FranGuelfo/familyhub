package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "parking_spots")
class ParkingSpotJpaEntity(
    @Id
    val id: UUID,

    @Column(name = "family_id", nullable = false)
    val familyId: UUID,

    @Column(name = "spot_number", nullable = false, length = 20)
    var spotNumber: String,

    @Column(name = "has_ev_charger", nullable = false)
    var hasEvCharger: Boolean,

    @Column(name = "status_type", nullable = false, length = 20)
    var statusType: String,

    @Column(name = "parked_vehicle_id")
    var parkedVehicleId: UUID? = null,

    @Column(name = "out_of_service_reason", length = 255)
    var outOfServiceReason: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParkingSpotJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}