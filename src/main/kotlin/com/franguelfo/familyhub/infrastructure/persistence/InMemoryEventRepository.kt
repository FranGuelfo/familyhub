package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.calendar.Event
import com.franguelfo.familyhub.domain.calendar.EventRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryEventRepository : EventRepository {

    private val storage = ConcurrentHashMap<UUID, Event>()

    override fun save(event: Event): Event {
        storage[event.id] = event
        return event
    }

    override fun findById(id: UUID): Event? = storage[id]

    override fun findByFamilyId(familyId: UUID): List<Event> =
        storage.values
            .filter { it.familyId == familyId }
            .sortedBy { it.startTime }

    override fun findByFamilyIdAndDateRange(
        familyId: UUID,
        startRange: LocalDateTime,
        endRange: LocalDateTime
    ): List<Event> =
        storage.values
            .filter { it.familyId == familyId && it.overlapsWith(startRange, endRange) }
            .sortedBy { it.startTime }

    override fun findAll(): List<Event> = storage.values.sortedBy { it.startTime }

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}