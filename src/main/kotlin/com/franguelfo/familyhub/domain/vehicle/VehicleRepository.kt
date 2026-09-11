package com.franguelfo.familyhub.domain.vehicle

import java.util.UUID

interface VehicleRepository {
    fun save(vehicle: Vehicle): Vehicle
    fun findById(id: UUID): Vehicle?
    fun findByFamilyId(familyId: UUID): List<Vehicle>
    fun findByLicensePlate(licensePlate: LicensePlate): Vehicle?
    fun findAll(): List<Vehicle>
    fun deleteById(id: UUID)
}