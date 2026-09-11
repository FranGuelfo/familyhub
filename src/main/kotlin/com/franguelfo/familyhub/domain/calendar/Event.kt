package com.franguelfo.familyhub.domain.calendar

import java.time.LocalDateTime
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val familyId: UUID,
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val category: EventCategory = EventCategory.GENERAL,
    val attendeeIds: Set<UUID> = emptySet(),
    val location: String? = null
) {
    init {
        require(!endTime.isBefore(startTime)) {
            "La fecha de fin ($endTime) no puede ser anterior a la fecha de inicio ($startTime)."
        }
        require(title.isNotBlank()) {
            "El título del evento no puede estar vacío."
        }
    }

    /**
     * Comprueba si este evento coincide o solapa en el tiempo con otro intervalo.
     */
    fun overlapsWith(otherStart: LocalDateTime, otherEnd: LocalDateTime): Boolean =
        startTime < otherEnd && endTime > otherStart

    fun addAttendee(memberId: UUID): Event =
        this.copy(attendeeIds = attendeeIds + memberId)

    fun removeAttendee(memberId: UUID): Event =
        this.copy(attendeeIds = attendeeIds - memberId)

    fun reschedule(newStart: LocalDateTime, newEnd: LocalDateTime): Event =
        this.copy(startTime = newStart, endTime = newEnd)
}