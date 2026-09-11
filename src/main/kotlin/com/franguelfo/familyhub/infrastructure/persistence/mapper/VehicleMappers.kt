package com.franguelfo.familyhub.infrastructure.persistence.mapper

import com.franguelfo.familyhub.domain.vehicle.LicensePlate
import com.franguelfo.familyhub.domain.vehicle.Vehicle
import com.franguelfo.familyhub.domain.vehicle.VehicleStatus
import com.franguelfo.familyhub.infrastructure.persistence.entity.VehicleJpaEntity

fun Vehicle.toEntity(): VehicleJpaEntity = VehicleJpaEntity(
    id = this.id,
    familyId = this.familyId,
    brand = this.brand,
    model = this.model,
    licensePlate = this.licensePlate.value, // Extrae el String del Value Class
    status = this.status.name,
    assignedMemberId = this.assignedMemberId
)

fun VehicleJpaEntity.toDomain(): Vehicle = Vehicle(
    id = this.id,
    familyId = this.familyId,
    brand = this.brand,
    model = this.model,
    licensePlate = LicensePlate(this.licensePlate), // Reconstruye el Value Class ejecutando sus validaciones
    status = VehicleStatus.valueOf(this.status),
    assignedMemberId = this.assignedMemberId
)