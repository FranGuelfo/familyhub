package com.franguelfo.familyhub.infrastructure.web.dto

import com.franguelfo.familyhub.domain.calendar.Event
import com.franguelfo.familyhub.domain.calendar.EventCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class CreateEventRequest(
    @field:NotNull(message = "El ID de la familia es obligatorio")
    val familyId: UUID,

    @field:NotBlank(message = "El título del evento es obligatorio")
    val title: String,

    val description: String? = null,

    @field:NotNull(message = "La fecha y hora de inicio son obligatorias")
    val startTime: LocalDateTime,

    @field:NotNull(message = "La fecha y hora de fin son obligatorias")
    val endTime: LocalDateTime,

    val category: EventCategory = EventCategory.GENERAL,
    val attendeeIds: Set<UUID> = emptySet(),
    val location: String? = null
)

data class EventResponse(
    val id: UUID,
    val familyId: UUID,
    val title: String,
    val description: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val category: EventCategory,
    val attendeeIds: Set<UUID>,
    val location: String?
)

fun Event.toResponse(): EventResponse = EventResponse(
    id = this.id,
    familyId = this.familyId,
    title = this.title,
    description = this.description,
    startTime = this.startTime,
    endTime = this.endTime,
    category = this.category,
    attendeeIds = this.attendeeIds,
    location = this.location
)