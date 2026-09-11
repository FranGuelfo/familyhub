package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.vehicle.LicensePlate
import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleRepository
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataVehicleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaVehicleRepositoryAdapter(
    private val springDataVehicleRepository: SpringDataVehicleRepository
) : VehicleRepository {

    override fun save(vehicle: Vehicle): Vehicle =
        springDataVehicleRepository.save(vehicle.toEntity()).toDomain()

    override fun findById(id: UUID): Vehicle? =
        springDataVehicleRepository.findByIdOrNull(id)?.toDomain()

    override fun findByFamilyId(familyId: UUID): List<Vehicle> =
        springDataVehicleRepository.findByFamilyId(familyId).map { it.toDomain() }

    override fun findByLicensePlate(licensePlate: LicensePlate): Vehicle? =
        springDataVehicleRepository.findByLicensePlate(licensePlate.value)?.toDomain()

    override fun findAll(): List<Vehicle> =
        springDataVehicleRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataVehicleRepository.deleteById(id)
    }
}