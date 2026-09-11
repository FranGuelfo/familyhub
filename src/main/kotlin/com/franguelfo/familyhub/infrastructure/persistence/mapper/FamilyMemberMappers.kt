package com.franguelfo.familyhub.infrastructure.persistence.mapper

import com.franguelfo.familyhub.domain.family.Family
import com.franguelfo.familyhub.domain.member.Member
import com.franguelfo.familyhub.domain.member.Role
import com.franguelfo.familyhub.infrastructure.persistence.entity.FamilyJpaEntity
import com.franguelfo.familyhub.infrastructure.persistence.entity.MemberJpaEntity

fun Member.toEntity(): MemberJpaEntity = MemberJpaEntity(
    id = this.id,
    name = this.name,
    email = this.email,
    role = this.role.name,
    password = this.password
)

fun MemberJpaEntity.toDomain(): Member = Member(
    id = this.id,
    name = this.name,
    email = this.email,
    role = Role.valueOf(this.role) ,
    password = this.password
)

fun Family.toEntity(): FamilyJpaEntity = FamilyJpaEntity(
    id = this.id,
    name = this.name,
    members = this.members.map { it.toEntity() }.toMutableSet()
)

fun FamilyJpaEntity.toDomain(): Family = Family(
    id = this.id,
    name = this.name,
    members = this.members.map { it.toDomain() }
)