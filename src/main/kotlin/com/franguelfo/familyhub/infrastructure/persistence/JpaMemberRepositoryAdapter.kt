package com.franguelfo.familyhub.infrastructure.persistence

import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.MemberRepository
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toDomain
import com.franguelfo.familyhub.infrastructure.persistence.mapper.toEntity
import com.franguelfo.familyhub.infrastructure.persistence.repository.SpringDataMemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaMemberRepositoryAdapter(
    private val springDataMemberRepository: SpringDataMemberRepository
) : MemberRepository {

    override fun save(member: Member): Member {
        val entity = member.toEntity()
        val saved = springDataMemberRepository.save(entity)
        return saved.toDomain()
    }

    // findByIdOrNull es la función de extensión idiomática de Spring Data para Kotlin
    override fun findById(id: UUID): Member? =
        springDataMemberRepository.findByIdOrNull(id)?.toDomain()

    override fun findByEmail(email: String): Member? =
        springDataMemberRepository.findByEmail(email)?.toDomain()

    override fun findAll(): List<Member> =
        springDataMemberRepository.findAll().map { it.toDomain() }

    override fun deleteById(id: UUID) {
        springDataMemberRepository.deleteById(id)
    }
}