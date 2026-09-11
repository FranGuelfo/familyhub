package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.SpotStatus
import com.franguelfo.familyhub.domain.vehicle.LicensePlate
import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    JpaParkingSpotRepositoryAdapter::class,
    JpaFamilyRepositoryAdapter::class,
    JpaVehicleRepositoryAdapter::class
)
class JpaParkingSpotRepositoryAdapterTest @Autowired constructor(
    private val parkingSpotRepository: JpaParkingSpotRepositoryAdapter,
    private val familyRepository: JpaFamilyRepositoryAdapter,
    private val vehicleRepository: JpaVehicleRepositoryAdapter
) {

    private lateinit var familyId: UUID

    @BeforeEach
    fun setUp() {
        val family = Family(id = UUID.randomUUID(), name = "Familia Garaje")
        familyRepository.save(family)
        familyId = family.id
    }

    @Test
    fun `debe persistir y mapear correctamente el estado SpotStatus Available`() {
        val spot = ParkingSpot(
            id = UUID.randomUUID(),
            familyId = familyId,
            spotNumber = "A-12",
            hasEvCharger = true,
            status = SpotStatus.Available
        )

        parkingSpotRepository.save(spot)

        val retrieved = parkingSpotRepository.findById(spot.id)

        assertThat(retrieved).isNotNull
        assertThat(retrieved!!.status).isInstanceOf(SpotStatus.Available::class.java)
    }

    @Test
    fun `debe persistir y mapear correctamente el estado SpotStatus Occupied con vehicleId`() {
        val vehicle = Vehicle(
            id = UUID.randomUUID(),
            familyId = familyId,
            brand = "Nissan",
            model = "Leaf",
            licensePlate = LicensePlate("9999ZZZ"),
            status = VehicleStatus.AVAILABLE
        )
        vehicleRepository.save(vehicle)

        val spot = ParkingSpot(
            id = UUID.randomUUID(),
            familyId = familyId,
            spotNumber = "B-05",
            hasEvCharger = true,
            status = SpotStatus.Occupied(vehicleId = vehicle.id)
        )

        parkingSpotRepository.save(spot)

        val retrieved = parkingSpotRepository.findById(spot.id)

        assertThat(retrieved).isNotNull
        assertThat(retrieved!!.status).isInstanceOf(SpotStatus.Occupied::class.java)
        val occupiedStatus = retrieved.status as SpotStatus.Occupied
        assertThat(occupiedStatus.vehicleId).isEqualTo(vehicle.id)
    }

    @Test
    fun `debe persistir y mapear correctamente el estado SpotStatus OutOfService con motivo`() {
        val spot = ParkingSpot(
            id = UUID.randomUUID(),
            familyId = familyId,
            spotNumber = "C-01",
            hasEvCharger = false,
            status = SpotStatus.OutOfService(reason = "Mantenimiento cargador")
        )

        parkingSpotRepository.save(spot)

        val retrieved = parkingSpotRepository.findById(spot.id)

        assertThat(retrieved).isNotNull
        assertThat(retrieved!!.status).isInstanceOf(SpotStatus.OutOfService::class.java)
        val outOfServiceStatus = retrieved.status as SpotStatus.OutOfService
        assertThat(outOfServiceStatus.reason).isEqualTo("Mantenimiento cargador")
    }
}