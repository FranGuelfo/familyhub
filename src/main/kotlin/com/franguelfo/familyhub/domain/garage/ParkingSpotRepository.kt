package com.franguelfo.familyhub.domain.garage

import java.util.UUID

interface ParkingSpotRepository {
    fun save(spot: ParkingSpot): ParkingSpot
    fun findById(id: UUID): ParkingSpot?
    fun findByFamilyId(familyId: UUID): List<ParkingSpot>
    fun findAll(): List<ParkingSpot>
    fun deleteById(id: UUID)
}