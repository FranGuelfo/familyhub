package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.VehicleJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataVehicleRepository : JpaRepository<VehicleJpaEntity, UUID> {
    fun findByFamilyId(familyId: UUID): List<VehicleJpaEntity>
    fun findByLicensePlate(licensePlate: String): VehicleJpaEntity?
}