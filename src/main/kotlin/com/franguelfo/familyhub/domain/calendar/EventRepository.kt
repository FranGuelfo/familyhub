package com.franguelfo.familyhub.domain.calendar

import java.time.LocalDateTime
import java.util.UUID

interface EventRepository {
    fun save(event: Event): Event
    fun findById(id: UUID): Event?
    fun findByFamilyId(familyId: UUID): List<Event>
    fun findByFamilyIdAndDateRange(
        familyId: UUID,
        startRange: LocalDateTime,
        endRange: LocalDateTime
    ): List<Event>
    fun findAll(): List<Event>
    fun deleteById(id: UUID)
}