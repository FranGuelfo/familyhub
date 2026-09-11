package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.ParkingSpotRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryParkingSpotRepository : ParkingSpotRepository {

    private val storage = ConcurrentHashMap<UUID, ParkingSpot>()

    override fun save(spot: ParkingSpot): ParkingSpot {
        storage[spot.id] = spot
        return spot
    }

    override fun findById(id: UUID): ParkingSpot? = storage[id]

    override fun findByFamilyId(familyId: UUID): List<ParkingSpot> =
        storage.values.filter { it.familyId == familyId }

    override fun findAll(): List<ParkingSpot> = storage.values.toList()

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}