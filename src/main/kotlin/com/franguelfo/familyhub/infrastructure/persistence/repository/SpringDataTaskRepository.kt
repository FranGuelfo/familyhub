package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.TaskJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataTaskRepository : JpaRepository<TaskJpaEntity, UUID> {
    fun findByAssignedTo(memberId: UUID): List<TaskJpaEntity>
    fun findByStatus(status: String): List<TaskJpaEntity>
}