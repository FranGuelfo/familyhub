package com.franguelfo.familyhub.domain.exception

import java.util.UUID

open class ResourceNotFoundException(message: String) : RuntimeException(message)

class FamilyNotFoundException(id: UUID) : ResourceNotFoundException("Familia con ID '$id' no encontrada")
class MemberNotFoundException(id: UUID) : ResourceNotFoundException("Miembro con ID '$id' no encontrado")
class TaskNotFoundException(id: UUID) : ResourceNotFoundException("Tarea con ID '$id' no encontrada")
class VehicleNotFoundException(id: UUID) : ResourceNotFoundException("Vehículo con ID '$id' no encontrado")
class ParkingSpotNotFoundException(id: UUID) : ResourceNotFoundException("Plaza de garaje con ID '$id' no encontrada")
class EventNotFoundException(id: UUID) : ResourceNotFoundException("Evento con ID '$id' no encontrado")