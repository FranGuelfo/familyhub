package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.MemberRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryMemberRepository : MemberRepository {

    private val storage = ConcurrentHashMap<UUID, Member>()

    override fun save(member: Member): Member {
        storage[member.id] = member
        return member
    }

    override fun findById(id: UUID): Member? = storage[id]

    override fun findByEmail(email: String): Member? =
        storage.values.firstOrNull { it.email.equals(email, ignoreCase = true) }

    override fun findAll(): List<Member> = storage.values.toList()

    override fun deleteById(id: UUID) {
        storage.remove(id)
    }
}