package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.calendar.Event
import com.franguelfo.familyhub.domain.calendar.EventRepository
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataCalendarEventRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Repository
class JpaCalendarEventRepositoryAdapter(
    private val springDataCalendarEventRepository: SpringDataCalendarEventRepository
) : EventRepository {

    @Transactional
    override fun save(event: Event): Event =
        springDataCalendarEventRepository.save(event.toEntity()).toDomain()

    override fun findById(id: UUID): Event? =
        springDataCalendarEventRepository.findByIdOrNull(id)?.toDomain()

    override fun findByFamilyId(familyId: UUID): List<Event> =
        springDataCalendarEventRepository.findByFamilyId(familyId).map { it.toDomain() }

    override fun findByFamilyIdAndDateRange(
        familyId: UUID,
        startRange: LocalDateTime,
        endRange: LocalDateTime
    ): List<Event> =
        springDataCalendarEventRepository.findByFamilyIdAndStartTimeBetween(familyId, startRange, endRange)
            .map { it.toDomain() }

    override fun findAll(): List<Event> =
        springDataCalendarEventRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataCalendarEventRepository.deleteById(id)
    }
}