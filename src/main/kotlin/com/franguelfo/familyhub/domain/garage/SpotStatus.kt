package com.franguelfo.familyhub.domain.garage

import java.util.UUID

sealed interface SpotStatus {
    data object Available : SpotStatus
    data class Occupied(val vehicleId: UUID) : SpotStatus
    data class OutOfService(val reason: String) : SpotStatus
}