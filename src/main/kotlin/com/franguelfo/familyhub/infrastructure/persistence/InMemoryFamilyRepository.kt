package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.domain.family.FamilyRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryFamilyRepository : FamilyRepository {

    // ConcurrentHashMap de la JDK estándar para soportar concurrencia entre hilos
    private val storage = ConcurrentHashMap<UUID, Family>()

    override fun save(family: Family): Family {
        storage[family.id] = family
        return family
    }

    override fun findById(id: UUID): Family? = storage[id]

    override fun findAll(): List<Family> = storage.values.toList()

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}