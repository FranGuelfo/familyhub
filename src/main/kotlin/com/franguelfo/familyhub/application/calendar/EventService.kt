package com.franguelfo.familyhub.application.calendar

import com.franguelfo.familyhub.domain.calendar.Event
import com.franguelfo.familyhub.domain.calendar.EventCategory
import com.franguelfo.familyhub.domain.calendar.EventRepository
import com.franguelfo.familyhub.domain.exception.EventNotFoundException
import com.franguelfo.familyhub.domain.exception.FamilyNotFoundException
import com.franguelfo.familyhub.domain.family.FamilyRepository
import com.franguelfo.familyhub.domain.member.MemberRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val familyRepository: FamilyRepository,
    private val memberRepository: MemberRepository
) {

    fun createEvent(
        familyId: UUID,
        title: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        description: String? = null,
        category: EventCategory = EventCategory.GENERAL,
        attendeeIds: Set<UUID> = emptySet(),
        location: String? = null
    ): Event {
        val family = familyRepository.findById(familyId) ?: throw FamilyNotFoundException(familyId)

        // Validamos que todos los asistentes pertenezcan a la familia
        val familyMemberIds = family.members.map { it.id }.toSet()
        val nonFamilyAttendees = attendeeIds.filterNot { it in familyMemberIds }
        if (nonFamilyAttendees.isNotEmpty()) {
            throw IllegalArgumentException(
                "Los siguientes miembros no pertenecen a la familia: $nonFamilyAttendees"
            )
        }

        val event = Event(
            familyId = familyId,
            title = title,
            description = description,
            startTime = startTime,
            endTime = endTime,
            category = category,
            attendeeIds = attendeeIds,
            location = location
        )

        return eventRepository.save(event)
    }

    fun getEventById(id: UUID): Event =
        eventRepository.findById(id) ?: throw EventNotFoundException(id)

    fun getEventsByFamily(familyId: UUID): List<Event> {
        if (familyRepository.findById(familyId) == null) {
            throw FamilyNotFoundException(familyId)
        }
        return eventRepository.findByFamilyId(familyId)
    }

    fun getEventsByFamilyAndRange(
        familyId: UUID,
        startRange: LocalDateTime,
        endRange: LocalDateTime
    ): List<Event> {
        if (familyRepository.findById(familyId) == null) {
            throw FamilyNotFoundException(familyId)
        }
        return eventRepository.findByFamilyIdAndDateRange(familyId, startRange, endRange)
    }

    fun addAttendee(eventId: UUID, memberId: UUID): Event {
        val event = getEventById(eventId)
        val family = familyRepository.findById(event.familyId) ?: throw FamilyNotFoundException(event.familyId)

        if (family.members.none { it.id == memberId }) {
            throw IllegalArgumentException("El miembro con ID '$memberId' no pertenece a la familia del evento")
        }

        return eventRepository.save(event.addAttendee(memberId))
    }

    fun removeAttendee(eventId: UUID, memberId: UUID): Event {
        val event = getEventById(eventId)
        return eventRepository.save(event.removeAttendee(memberId))
    }

    fun deleteEvent(id: UUID) {
        getEventById(id) // Lanza 404 si no existe
        eventRepository.deleteById(id)
    }
}