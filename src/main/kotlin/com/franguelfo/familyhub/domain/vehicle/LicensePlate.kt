package com.franguelfo.familyhub.domain.vehicle

@JvmInline
value class LicensePlate(val value: String) {
    init {
        val normalized = value.trim().uppercase()
        require(normalized.isNotBlank()) { "La matrícula no puede estar vacía" }
    }
}