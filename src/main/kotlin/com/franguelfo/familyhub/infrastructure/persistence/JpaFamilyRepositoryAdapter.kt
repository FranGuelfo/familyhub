package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.domain.family.FamilyRepository
import com.franguelfo.familyhub.infrastructure.persistence.entity.FamilyJpaEntity
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataFamilyRepository
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataMemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class JpaFamilyRepositoryAdapter(
    private val springDataFamilyRepository: SpringDataFamilyRepository,
    private val springDataMemberRepository: SpringDataMemberRepository
) : FamilyRepository {

    @Transactional
    override fun save(family: Family): Family {
        // Enlaza con entidades administradas existentes en BD para evitar duplicados en la tabla intermedia
        val managedMembers = family.members.mapNotNull {
            springDataMemberRepository.findByIdOrNull(it.id) ?: it.toEntity()
        }.toMutableSet()

        val familyEntity = FamilyJpaEntity(
            id = family.id,
            name = family.name,
            members = managedMembers
        )

        val saved = springDataFamilyRepository.save(familyEntity)
        return saved.toDomain()
    }

    override fun findById(id: UUID): Family? =
        springDataFamilyRepository.findByIdOrNull(id)?.toDomain()

    override fun findAll(): List<Family> =
        springDataFamilyRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataFamilyRepository.deleteById(id)
    }
}