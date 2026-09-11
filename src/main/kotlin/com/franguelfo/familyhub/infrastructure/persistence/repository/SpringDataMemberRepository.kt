package com.franguelfo.familyhub.infrastructure.persistence.repository

import com.franguelfo.familyhub.infrastructure.persistence.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataMemberRepository : JpaRepository<MemberJpaEntity, UUID> {
    fun findByEmail(email: String): MemberJpaEntity?
}