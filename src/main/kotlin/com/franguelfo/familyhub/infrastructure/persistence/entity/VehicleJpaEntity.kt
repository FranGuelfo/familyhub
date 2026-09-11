package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "vehicles")
class VehicleJpaEntity(
    @Id
    val id: UUID,

    @Column(name = "family_id", nullable = false)
    val familyId: UUID,

    @Column(nullable = false, length = 50)
    var brand: String,

    @Column(nullable = false, length = 50)
    var model: String,

    // Aplanamos el Value Class a un String plano en la tabla
    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    var licensePlate: String,

    @Column(nullable = false, length = 20)
    var status: String,

    @Column(name = "assigned_member_id")
    var assignedMemberId: UUID? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VehicleJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}