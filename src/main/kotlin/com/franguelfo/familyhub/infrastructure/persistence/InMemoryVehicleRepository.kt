package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.vehicle.LicensePlate
import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryVehicleRepository : VehicleRepository {

    private val storage = ConcurrentHashMap<UUID, Vehicle>()

    override fun save(vehicle: Vehicle): Vehicle {
        storage[vehicle.id] = vehicle
        return vehicle
    }

    override fun findById(id: UUID): Vehicle? = storage[id]

    override fun findByFamilyId(familyId: UUID): List<Vehicle> =
        storage.values.filter { it.familyId == familyId }

    override fun findByLicensePlate(licensePlate: LicensePlate): Vehicle? =
        storage.values.firstOrNull { it.licensePlate.value.equals(licensePlate.value, ignoreCase = true) }

    override fun findAll(): List<Vehicle> = storage.values.toList()

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}