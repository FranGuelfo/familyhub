package com.franguelfo.familyhub.domain.member

import java.util.UUID

data class Member(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val role: Role = Role.MEMBER,
    val password: String = ""
)