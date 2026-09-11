package com.franguelfo.familyhub.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "members")
class MemberJpaEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(nullable = false, unique = true, length = 150)
    var email: String,

    @Column(nullable = false, length = 20)
    var role: String,

    @Column(nullable = false, length = 255)
    var password: String = ""
    
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MemberJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}