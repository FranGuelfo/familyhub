package com.franguelfo.familyhub.application.vehicle

import com.franguelfo.familyhub.domain.exception.FamilyNotFoundException
import com.franguelfo.familyhub.domain.exception.MemberNotFoundException
import com.franguelfo.familyhub.domain.exception.VehicleNotFoundException
import com.franguelfo.familyhub.domain.family.FamilyRepository
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.domain.vehicle.LicensePlate
import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val familyRepository: FamilyRepository,
    private val memberRepository: MemberRepository
) {

    fun registerVehicle(
        familyId: UUID,
        brand: String,
        model: String,
        licensePlateText: String
    ): Vehicle {
        // 1. Validamos que la familia exista
        if (familyRepository.findById(familyId) == null) {
            throw FamilyNotFoundException(familyId)
        }

        // 2. Instanciamos el Value Class (ejecuta automáticamente sus validaciones de formato)
        val plate = LicensePlate(licensePlateText)

        // 3. Regla de negocio: la matrícula debe ser única en el sistema
        vehicleRepository.findByLicensePlate(plate)?.let {
            throw IllegalArgumentException("Ya existe un vehículo registrado con la matrícula '${plate.value}'")
        }

        val vehicle = Vehicle(
            familyId = familyId,
            brand = brand,
            model = model,
            licensePlate = plate
        )

        return vehicleRepository.save(vehicle)
    }

    fun assignVehicle(vehicleId: UUID, memberId: UUID): Vehicle {
        val vehicle = getVehicleById(vehicleId)

        // Comprobamos que el miembro existe
        memberRepository.findById(memberId) ?: throw MemberNotFoundException(memberId)

        // Regla de consistencia: el miembro debe pertenecer a la familia propietaria del vehículo
        val family = familyRepository.findById(vehicle.familyId) ?: throw FamilyNotFoundException(vehicle.familyId)
        val belongsToFamily = family.members.any { it.id == memberId }

        if (!belongsToFamily) {
            throw IllegalArgumentException("El miembro no pertenece a la familia propietaria de este vehículo")
        }

        val updatedVehicle = vehicle.assignTo(memberId)
        return vehicleRepository.save(updatedVehicle)
    }

    fun releaseVehicle(vehicleId: UUID): Vehicle {
        val vehicle = getVehicleById(vehicleId)
        return vehicleRepository.save(vehicle.release())
    }

    fun getVehicleById(id: UUID): Vehicle =
        vehicleRepository.findById(id) ?: throw VehicleNotFoundException(id)

    fun getVehiclesByFamily(familyId: UUID): List<Vehicle> =
        vehicleRepository.findByFamilyId(familyId)

    fun getAllVehicles(): List<Vehicle> = vehicleRepository.findAll()
}