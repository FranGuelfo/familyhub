package com.franguelfo.familyhub.infrastructure.persistence.mapper

import com.franguelfo.familyhub.domain.calendar.Event
import com.franguelfo.familyhub.domain.calendar.EventCategory
import com.franguelfo.familyhub.infrastructure.persistence.entity.CalendarEventJpaEntity

fun Event.toEntity(): CalendarEventJpaEntity = CalendarEventJpaEntity(
    id = this.id,
    familyId = this.familyId,
    title = this.title,
    description = this.description,
    startTime = this.startTime,
    endTime = this.endTime,
    category = this.category.name,
    location = this.location,
    attendeeIds = this.attendeeIds.toMutableSet()
)

fun CalendarEventJpaEntity.toDomain(): Event = Event(
    id = this.id,
    familyId = this.familyId,
    title = this.title,
    description = this.description,
    startTime = this.startTime,
    endTime = this.endTime,
    category = EventCategory.valueOf(this.category),
    location = this.location,
    attendeeIds = this.attendeeIds.toSet()
)