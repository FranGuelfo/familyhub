package com.franguelfo.familyhub.infrastructure.web.controller

import com.franguelfo.familyhub.application.calendar.EventService
import com.franguelfo.familyhub.infrastructure.web.dto.CreateEventRequest
import com.franguelfo.familyhub.infrastructure.web.dto.EventResponse
import com.franguelfo.familyhub.infrastructure.web.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/api/events")
@Tag(name = "Calendario", description = "Endpoints para gestionar el calendario")
class EventController(
    private val eventService: EventService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo evento", description = "Crea un evento")
    fun createEvent(@Valid @RequestBody request: CreateEventRequest): EventResponse {
        val created = eventService.createEvent(
            familyId = request.familyId,
            title = request.title,
            startTime = request.startTime,
            endTime = request.endTime,
            description = request.description,
            category = request.category,
            attendeeIds = request.attendeeIds,
            location = request.location
        )
        return created.toResponse()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID", description = "Devuelve el detalle de un evento a partir de su identificador")
    fun getEventById(@PathVariable id: UUID): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.getEventById(id).toResponse())

    @GetMapping
    @Operation(summary = "Listar eventos de una familia", description = "Devuelve los eventos de una familia, opcionalmente filtrados por rango de fechas")
    fun getEventsByFamily(
        @RequestParam familyId: UUID,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime?
    ): List<EventResponse> {
        val events = if (start != null && end != null) {
            eventService.getEventsByFamilyAndRange(familyId, start, end)
        } else {
            eventService.getEventsByFamily(familyId)
        }
        return events.map { it.toResponse() }
    }

    @PostMapping("/{id}/attendees/{memberId}")
    @Operation(summary = "Añadir asistente a un evento", description = "Asocia un miembro como asistente de un evento")
    fun addAttendee(
        @PathVariable id: UUID,
        @PathVariable memberId: UUID
    ): ResponseEntity<EventResponse> {
        val updated = eventService.addAttendee(id, memberId)
        return ResponseEntity.ok(updated.toResponse())
    }

    @DeleteMapping("/{id}/attendees/{memberId}")
    @Operation(summary = "Eliminar asistente de un evento", description = "Quita un miembro de la lista de asistentes de un evento")
    fun removeAttendee(
        @PathVariable id: UUID,
        @PathVariable memberId: UUID
    ): ResponseEntity<EventResponse> {
        val updated = eventService.removeAttendee(id, memberId)
        return ResponseEntity.ok(updated.toResponse())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar evento", description = "Elimina un evento por su identificador")
    fun deleteEvent(@PathVariable id: UUID) {
        eventService.deleteEvent(id)
    }
}
