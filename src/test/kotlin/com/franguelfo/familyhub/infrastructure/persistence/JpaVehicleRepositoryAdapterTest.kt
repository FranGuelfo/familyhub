package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.family.Family
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
@Import(JpaVehicleRepositoryAdapter::class, JpaFamilyRepositoryAdapter::class)
class JpaVehicleRepositoryAdapterTest @Autowired constructor(
    private val vehicleRepository: JpaVehicleRepositoryAdapter,
    private val familyRepository: JpaFamilyRepositoryAdapter
) {

    private lateinit var familyId: UUID

    @BeforeEach
    fun setUp() {
        // Creamos la familia previa para cumplir la FK de PostgreSQL
        val family = Family(id = UUID.randomUUID(), name = "Familia Test")
        familyRepository.save(family)
        familyId = family.id
    }

    @Test
    fun `debe persistir y recuperar un vehiculo aplanando y reconstruyendo su LicensePlate`() {
        val vehicle = Vehicle(
            id = UUID.randomUUID(),
            familyId = familyId,
            brand = "Toyota",
            model = "Corolla",
            licensePlate = LicensePlate("1234BBB"),
            status = VehicleStatus.AVAILABLE
        )

        vehicleRepository.save(vehicle)

        val retrieved = vehicleRepository.findById(vehicle.id)

        assertThat(retrieved).isNotNull
        assertThat(retrieved!!.brand).isEqualTo("Toyota")
        assertThat(retrieved.licensePlate).isEqualTo(LicensePlate("1234BBB"))
        assertThat(retrieved.licensePlate.value).isEqualTo("1234BBB")
    }

    @Test
    fun `debe buscar correctamente un vehiculo por su LicensePlate`() {
        val plate = LicensePlate("5678CCC")
        val vehicle = Vehicle(
            id = UUID.randomUUID(),
            familyId = familyId,
            brand = "Tesla",
            model = "Model 3",
            licensePlate = plate,
            status = VehicleStatus.IN_USE
        )

        vehicleRepository.save(vehicle)

        val found = vehicleRepository.findByLicensePlate(plate)

        assertThat(found).isNotNull
        assertThat(found!!.id).isEqualTo(vehicle.id)
        assertThat(found.model).isEqualTo("Model 3")
    }
}