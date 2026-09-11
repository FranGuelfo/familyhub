package com.franguelfo.familyhub.domain.family

import java.util.UUID

interface FamilyRepository {
    fun save(family: Family): Family
    fun findById(id: UUID): Family?
    fun findAll(): List<Family>
    fun deleteById(id: UUID)
}