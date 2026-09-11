package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.garage.ParkingSpot
import com.franguelfo.familyhub.domain.garage.ParkingSpotRepository
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataParkingSpotRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaParkingSpotRepositoryAdapter(
    private val springDataParkingSpotRepository: SpringDataParkingSpotRepository
) : ParkingSpotRepository {

    override fun save(spot: ParkingSpot): ParkingSpot =
        springDataParkingSpotRepository.save(spot.toEntity()).toDomain()

    override fun findById(id: UUID): ParkingSpot? =
        springDataParkingSpotRepository.findByIdOrNull(id)?.toDomain()

    override fun findByFamilyId(familyId: UUID): List<ParkingSpot> =
        springDataParkingSpotRepository.findByFamilyId(familyId).map { it.toDomain() }

    override fun findAll(): List<ParkingSpot> =
        springDataParkingSpotRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataParkingSpotRepository.deleteById(id)
    }
}