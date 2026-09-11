package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.CalendarEventJpaEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

interface SpringDataCalendarEventRepository : JpaRepository<CalendarEventJpaEntity, UUID> {

    @EntityGraph(attributePaths = ["attendeeIds"])
    override fun findById(id: UUID): Optional<CalendarEventJpaEntity>

    @EntityGraph(attributePaths = ["attendeeIds"])
    fun findByFamilyId(familyId: UUID): List<CalendarEventJpaEntity>

    @EntityGraph(attributePaths = ["attendeeIds"])
    fun findByFamilyIdAndStartTimeBetween(
        familyId: UUID,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<CalendarEventJpaEntity>
}