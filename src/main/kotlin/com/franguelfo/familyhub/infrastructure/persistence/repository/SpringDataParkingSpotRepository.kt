package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.ParkingSpotJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataParkingSpotRepository : JpaRepository<ParkingSpotJpaEntity, UUID> {
    fun findByFamilyId(familyId: UUID): List<ParkingSpotJpaEntity>
}