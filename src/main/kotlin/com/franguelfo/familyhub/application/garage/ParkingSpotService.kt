package com.franguelfo.familyhub.application.garage

import com.franguelfo.familyhub.domain.exception.FamilyNotFoundException
import com.franguelfo.familyhub.domain.exception.ParkingSpotNotFoundException
import com.franguelfo.familyhub.domain.exception.VehicleNotFoundException
import com.franguelfo.familyhub.domain.family.FamilyRepository
import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.ParkingSpotRepository
import com.franguelfo.familyhub.domain.vehicle.VehicleRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ParkingSpotService(
    private val parkingSpotRepository: ParkingSpotRepository,
    private val familyRepository: FamilyRepository,
    private val vehicleRepository: VehicleRepository
) {

    fun createSpot(familyId: UUID, spotNumber: String, hasEvCharger: Boolean = false): ParkingSpot {
        if (familyRepository.findById(familyId) == null) {
            throw FamilyNotFoundException(familyId)
        }

        val spot = ParkingSpot(
            familyId = familyId,
            spotNumber = spotNumber,
            hasEvCharger = hasEvCharger
        )
        return parkingSpotRepository.save(spot)
    }

    fun parkVehicle(spotId: UUID, vehicleId: UUID): ParkingSpot {
        val spot = getSpotById(spotId)
        val vehicle = vehicleRepository.findById(vehicleId) ?: throw VehicleNotFoundException(vehicleId)

        // Regla de consistencia: el vehículo debe pertenecer a la misma familia de la plaza
        if (vehicle.familyId != spot.familyId) {
            throw IllegalArgumentException("El vehículo no pertenece a la misma familia propietaria de la plaza de garaje.")
        }

        val updatedSpot = spot.park(vehicleId)
        return parkingSpotRepository.save(updatedSpot)
    }

    fun vacateSpot(spotId: UUID): ParkingSpot {
        val spot = getSpotById(spotId)
        val updatedSpot = spot.vacate()
        return parkingSpotRepository.save(updatedSpot)
    }

    fun getSpotById(id: UUID): ParkingSpot =
        parkingSpotRepository.findById(id) ?: throw ParkingSpotNotFoundException(id)

    fun getSpotsByFamily(familyId: UUID): List<ParkingSpot> =
        parkingSpotRepository.findByFamilyId(familyId)

    fun getAllSpots(): List<ParkingSpot> = parkingSpotRepository.findAll()
}