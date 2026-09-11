package com.franguelfo.familyhub.domain.member

import java.util.UUID

interface MemberRepository {
    fun save(member: Member): Member
    fun findById(id: UUID): Member?
    fun findByEmail(email: String): Member?
    fun findAll(): List<Member>
    fun deleteById(id: UUID)
}