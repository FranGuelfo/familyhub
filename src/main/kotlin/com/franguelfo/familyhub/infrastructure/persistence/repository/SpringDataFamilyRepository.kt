package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.FamilyJpaEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface SpringDataFamilyRepository : JpaRepository<FamilyJpaEntity, UUID> {
    // @EntityGraph carga los miembros en una sola consulta JOIN, evitando LazyInitializationException y el problema N+1
    @EntityGraph(attributePaths = ["members"])
    override fun findById(id: UUID): Optional<FamilyJpaEntity>

    @EntityGraph(attributePaths = ["members"])
    override fun findAll(): List<FamilyJpaEntity>
}